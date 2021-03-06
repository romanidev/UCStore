/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ultracolor.controllers.process;

import com.ultracolor.controllers.util.JsfUtil;
import com.ultracolor.controllers.util.Log4jConfig;
import com.ultracolor.entities.Personal;
import com.ultracolor.entities.Usuario;
import com.ultracolor.facades.PersonalFacadeLocal;
import com.ultracolor.facades.UsuarioFacadeLocal;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Raul
 */
@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

  @EJB
  private com.ultracolor.facades.UsuarioFacadeLocal ejbFacadeUsuario;

  @EJB
  private com.ultracolor.facades.PersonalFacadeLocal ejbFacadePersonal;

  private Personal personal;
  private Usuario usuario;
  private UploadedFile file;
  private StreamedContent image;
  private String pathImage;

  final static Logger logger = Log4jConfig.getLogger(LoginController.class.getName());

  @PostConstruct
  private void init() {
    usuario = new Usuario();
    personal = new Personal();
    file = null;
    String projectStage = FacesContext.getCurrentInstance().getApplication().getProjectStage().toString();
    if (projectStage.equals("Production")) {
      pathImage = ResourceBundle.getBundle("/setup/deploy").getString("productionUsuarioPhotoPath");
    } else if (projectStage.equals("Development")) {
      pathImage = ResourceBundle.getBundle("/setup/deploy").getString("developmentUsuarioPhotoPath");
    }
    logger.info("Project stage : " + projectStage);
  }

  public void checkSession() {

    FacesContext context = FacesContext.getCurrentInstance();

    Usuario u = (Usuario) context.getExternalContext().getSessionMap().get("user");
    if (u == null) {
      
      RequestContext.getCurrentInstance().update("growl");
      context.getExternalContext().getFlash().setKeepMessages(true);
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "No ha iniciado sesión"));
      
      context.getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/login?faces-redirect=true");
      logger.info("exito -- seguridad de sesión"); // Anybody can watch a page without a session
    } else {
      logger.info("Sesion de usuario existente!");
    }
  }

  public String validar() {

    Usuario u = ejbFacadeUsuario.validar(usuario.getUsuario(), usuario.getClave());
    if (u != null) {
      usuario = u;
      personal = usuario.getIdPersonal();

      //creamos una sesion jsf usuario
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", u);

      RequestContext.getCurrentInstance().update("growl");
      FacesContext context = FacesContext.getCurrentInstance();
      context.getExternalContext().getFlash().setKeepMessages(true);
      JsfUtil.addSuccessMessage("Bienvenido " + personal.getNombre() + " " + personal.getApellido());

      return "main?faces-redirect=true"; // Pagina a Redireccionar
    } else {
      RequestContext.getCurrentInstance().update("growl");
      FacesContext context = FacesContext.getCurrentInstance();
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario o contraseña es invalido"));
      
      return "";
    }
  }

  /**
   * Listen for logout button clicks on the #{loginController.logout} action and
   * navigates to login screen.
   */
  public void logout() {

    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    //logger.log(Level.INFO, "User ({0}) loging out #" , request.getUserPrincipal().getName());
    if (session != null) {
      session.invalidate();
    }
    FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "login?faces-redirect=true");
  }

  public void update() throws FileNotFoundException {
    if (file != null) {
      uploadFoto();
      logger.info("Upload foto OK");
    }
    persist(JsfUtil.PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PersonalUpdated"));
    //load photo  - Si fuera Ajax
//      FileInputStream stream = new FileInputStream(pathImage + personal.getIdPersonal()+ ".jpg");
//      image = new DefaultStreamedContent(stream, "image/jpg");
//      logger.info("Upload photo OK");
  }

  private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
    if (personal != null) {
      try {
        if (persistAction != JsfUtil.PersistAction.DELETE) {
          ejbFacadePersonal.edit(personal);
          ejbFacadeUsuario.edit(usuario);
          logger.info("EDIT loginController OK");
        } else {
          // ejbFacade.remove(personal); don't supported methos
        }
        JsfUtil.addSuccessMessage(successMessage);
      } catch (EJBException ex) {
        String msg = "";
        Throwable cause = ex.getCause();
        if (cause != null) {
          msg = cause.getLocalizedMessage();
        }
        if (msg.length() > 0) {
          JsfUtil.addErrorMessage(msg);
        } else {
          JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
      } catch (Exception ex) {
        java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
      }
    }
  }

  public StreamedContent getImage() {
    
    File foto = new File(pathImage + usuario.getIdUsuario() + ".jpg");
    if(foto.exists() && ! foto.isDirectory()) {
      try {
        FileInputStream stream = new FileInputStream(foto);
        image = new DefaultStreamedContent(stream, "image/jpg");
      } catch (FileNotFoundException ex) { ex.printStackTrace();}
    } else { // cargar photo por defecto
      
    }
    return image;
  }

  public void uploadFoto() {

    try {
      String fileName = usuario.getIdUsuario() + ".jpg";
      if (personal.getIdPersonal() == null) //Si se va a crear
      {
        image = new DefaultStreamedContent(new ByteArrayInputStream(file.getContents()), "image/png");
      }
//      file.get
      InputStream in = file.getInputstream();
      String destination = pathImage;

      // write the inputStream to a FileOutputStream
      OutputStream out = new FileOutputStream(new File(destination + fileName));

      int read = 0;
      byte[] bytes = new byte[1024];

      while ((read = in.read(bytes)) != -1) {
        out.write(bytes, 0, read);
      }

      in.close();
      out.flush();
      out.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Personal getPersonal() {
    return personal;
  }

  public void setPersonal(Personal personal) {
    this.personal = personal;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }
  
  

  public UploadedFile getFile() {
    return file;
  }

  public void setFile(UploadedFile file) {
    this.file = file;
  }

}
