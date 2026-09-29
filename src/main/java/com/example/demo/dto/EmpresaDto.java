package com.example.demo.dto;

public class EmpresaDto {
    private String ruc;
    private String razonsocial;
    private String direccion;
    private String correo;
    private String telefono;

    // Getters y Setters
    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public String getRazonsocial() { return razonsocial; }
    public void setRazonsocial(String razonsocial) { this.razonsocial = razonsocial; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}