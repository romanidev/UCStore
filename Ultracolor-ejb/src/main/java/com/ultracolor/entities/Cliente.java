/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ultracolor.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Raul
 */
@Entity
@Table(name = "cliente")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c"),
  @NamedQuery(name = "Cliente.findByIdCliente", query = "SELECT c FROM Cliente c WHERE c.idCliente = :idCliente"),
  @NamedQuery(name = "Cliente.findByNombre", query = "SELECT c FROM Cliente c WHERE c.nombre = :nombre"),
  @NamedQuery(name = "Cliente.findByApelllido", query = "SELECT c FROM Cliente c WHERE c.apelllido = :apelllido"),
  @NamedQuery(name = "Cliente.findByDni", query = "SELECT c FROM Cliente c WHERE c.dni = :dni"),
  @NamedQuery(name = "Cliente.findByRuc", query = "SELECT c FROM Cliente c WHERE c.ruc = :ruc"),
  @NamedQuery(name = "Cliente.findByDireccion", query = "SELECT c FROM Cliente c WHERE c.direccion = :direccion"),
  @NamedQuery(name = "Cliente.findByCelular", query = "SELECT c FROM Cliente c WHERE c.celular = :celular"),
  @NamedQuery(name = "Cliente.findByTelefono", query = "SELECT c FROM Cliente c WHERE c.telefono = :telefono"),
  @NamedQuery(name = "Cliente.findBySexo", query = "SELECT c FROM Cliente c WHERE c.sexo = :sexo"),
  @NamedQuery(name = "Cliente.findByEdad", query = "SELECT c FROM Cliente c WHERE c.edad = :edad"),
  @NamedQuery(name = "Cliente.findByCorreo", query = "SELECT c FROM Cliente c WHERE c.correo = :correo"),
  @NamedQuery(name = "Cliente.findByLugarNacimiento", query = "SELECT c FROM Cliente c WHERE c.lugarNacimiento = :lugarNacimiento"),
  @NamedQuery(name = "Cliente.findByFechaNacimiento", query = "SELECT c FROM Cliente c WHERE c.fechaNacimiento = :fechaNacimiento"),
  @NamedQuery(name = "Cliente.findByReligion", query = "SELECT c FROM Cliente c WHERE c.religion = :religion"),
  @NamedQuery(name = "Cliente.findByTipo", query = "SELECT c FROM Cliente c WHERE c.tipo = :tipo"),
  @NamedQuery(name = "Cliente.findByCategoria", query = "SELECT c FROM Cliente c WHERE c.categoria = :categoria"),
  @NamedQuery(name = "Cliente.findByFechaCreacion", query = "SELECT c FROM Cliente c WHERE c.fechaCreacion = :fechaCreacion")})
public class Cliente implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idCliente")
  private Integer idCliente;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "nombre")
  private String nombre;
  @Size(max = 100)
  @Column(name = "apelllido")
  private String apelllido;
  @Size(max = 8)
  @Column(name = "DNI")
  private String dni;
  @Size(max = 11)
  @Column(name = "RUC")
  private String ruc;
  @Size(max = 100)
  @Column(name = "direccion")
  private String direccion;
  @Size(max = 20)
  @Column(name = "celular")
  private String celular;
  @Size(max = 20)
  @Column(name = "telefono")
  private String telefono;
  @Size(max = 10)
  @Column(name = "sexo")
  private String sexo;
  @Size(max = 3)
  @Column(name = "edad")
  private String edad;
  @Size(max = 50)
  @Column(name = "correo")
  private String correo;
  @Size(max = 100)
  @Column(name = "lugarNacimiento")
  private String lugarNacimiento;
  @Column(name = "fechaNacimiento")
  @Temporal(TemporalType.DATE)
  private Date fechaNacimiento;
  @Size(max = 50)
  @Column(name = "religion")
  private String religion;
  @Size(max = 10)
  @Column(name = "tipo")
  private String tipo;
  @Size(max = 10)
  @Column(name = "categoria")
  private String categoria;
  @Basic(optional = false)
  @NotNull
  @Column(name = "fechaCreacion")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaCreacion;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCliente")
  private List<Venta> ventaList;

  public Cliente() {
  }

  public Cliente(Integer idCliente) {
    this.idCliente = idCliente;
  }

  public Cliente(Integer idCliente, String nombre, Date fechaCreacion) {
    this.idCliente = idCliente;
    this.nombre = nombre;
    this.fechaCreacion = fechaCreacion;
  }

  public Integer getIdCliente() {
    return idCliente;
  }

  public void setIdCliente(Integer idCliente) {
    this.idCliente = idCliente;
  }

  public String getNombre() {
    return nombre;
  }
  
  public String getNombreCompleto() {
    return nombre + " " + apelllido;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApelllido() {
    return apelllido;
  }

  public void setApelllido(String apelllido) {
    this.apelllido = apelllido;
  }

  public String getDni() {
    return dni;
  }

  public void setDni(String dni) {
    this.dni = dni;
  }

  public String getRuc() {
    return ruc;
  }

  public void setRuc(String ruc) {
    this.ruc = ruc;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public String getCelular() {
    return celular;
  }

  public void setCelular(String celular) {
    this.celular = celular;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getSexo() {
    return sexo;
  }

  public void setSexo(String sexo) {
    this.sexo = sexo;
  }

  public String getEdad() {
    return edad;
  }

  public void setEdad(String edad) {
    this.edad = edad;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public String getLugarNacimiento() {
    return lugarNacimiento;
  }

  public void setLugarNacimiento(String lugarNacimiento) {
    this.lugarNacimiento = lugarNacimiento;
  }

  public Date getFechaNacimiento() {
    return fechaNacimiento;
  }

  public void setFechaNacimiento(Date fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }

  public String getReligion() {
    return religion;
  }

  public void setReligion(String religion) {
    this.religion = religion;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public Date getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(Date fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  @XmlTransient
  public List<Venta> getVentaList() {
    return ventaList;
  }

  public void setVentaList(List<Venta> ventaList) {
    this.ventaList = ventaList;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idCliente != null ? idCliente.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Cliente)) {
      return false;
    }
    Cliente other = (Cliente) object;
    if ((this.idCliente == null && other.idCliente != null) || (this.idCliente != null && !this.idCliente.equals(other.idCliente))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.ultracolor.entities.Cliente[ idCliente=" + idCliente + " ]";
  }
  
}
