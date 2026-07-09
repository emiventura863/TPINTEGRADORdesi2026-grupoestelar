package com.example.demo.entidades;

import com.example.demo.enums.EstadoPropiedad;
import com.example.demo.enums.TipoPropiedad;
import jakarta.persistence.*; // importa todas las anotaciones de JPA (Id, Entity, ManyToOne)
import java.util.ArrayList; // necesario para inicializar la lista del historial
import java.util.List; // necesario para manejar colecciones de datos

@Entity
public class Propiedad {
    
	@Id // genera automaticamente un ID unico en la base de datos
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String direccion;
	private String ciudad;
    
    @Enumerated(EnumType.STRING)
    private TipoPropiedad tipo; // registra si es CASA, DEPARTAMENTO, LOCAL u OTRO

    private Integer cantidadAmbientes; // tiene que ser un numero entero positivo
    private Double metrosCuadrados; // tiene que ser un numero positivo
    
    @Column(length = 500) // le da mas margen de caracteres en la BD
    private String descripcion; // descripcion general del inmueble
    
    @Column(length = 500)
    private String comodidades; // campo adicional incluido en el diagrama de clases

	// Relacion con el Propietario (Muchas propiedades pueden pertenecer a una misma Persona)
    @ManyToOne
    @JoinColumn(name = "propietario_id") // nombre de la columna clave foranea en la tabla Propiedad
    private Persona propietario;

    // una propiedad tiene muchos cambios de estado guardados
    // CascadeType.ALL asegura que si se guarda/elimina una propiedad, impacte en su historial de estados
    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialEstadoPropiedad> historialEstados = new ArrayList<>();

	// almacena el estado del contrato como un (string) en la base de datos, en vez
	// de un valor numerico. Esto hace que sea mas legible y facil de mantener.
	@Enumerated(EnumType.STRING)
	private EstadoPropiedad estado = EstadoPropiedad.DISPONIBLE;

	// indica si la propiedad ha sido eliminada logicamente, sin borrarla fisicamente de la BD.
	private Boolean eliminado = false;

    // constructor vacio (Obligatorio para que JPA/Hibernate cargue datos de la BD)
    public Propiedad() {
        this.estado = EstadoPropiedad.DISPONIBLE; // Por defecto, toda propiedad nueva nace DISPONIBLE
        this.eliminado = false;                   // Por defecto, no está eliminada
    }

    // constructor completo (crea propiedades con todos sus datos)
    public Propiedad(Long id, String direccion, String ciudad, TipoPropiedad tipo, Integer cantidadAmbientes, 
                     Double metrosCuadrados, String descripcion, String comodidades, EstadoPropiedad estado, Persona propietario) {
        super();
        this.id = id;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.tipo = tipo;
        this.cantidadAmbientes = cantidadAmbientes;
        this.metrosCuadrados = metrosCuadrados;
        this.descripcion = descripcion;
        this.comodidades = comodidades;
        // si al crearla no le pasamos estado, se asegura de ponerle DISPONIBLE por defecto
        this.estado = (estado != null) ? estado : EstadoPropiedad.DISPONIBLE; 
        this.propietario = propietario;
        this.eliminado = false; // nace activa en el sistema
    }

	// GETTERS Y SETTERS

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
    
	public TipoPropiedad getTipo() {
        return tipo;
    }

    public void setTipo(TipoPropiedad tipo) {
        this.tipo = tipo;
    }

    public Integer getCantidadAmbientes() {
        return cantidadAmbientes;
    }

    public void setCantidadAmbientes(Integer cantidadAmbientes) {
        this.cantidadAmbientes = cantidadAmbientes;
    }

    public Double getMetrosCuadrados() {
        return metrosCuadrados;
    }

    public void setMetrosCuadrados(Double metrosCuadrados) {
        this.metrosCuadrados = metrosCuadrados;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getComodidades() {
        return comodidades;
    }

    public void setComodidades(String comodidades) {
        this.comodidades = comodidades;
    }

    public Persona getPropietario() {
        return propietario;
    }

    public void setPropietario(Persona propietario) {
        this.propietario = propietario;
    }

    public List<HistorialEstadoPropiedad> getHistorialEstados() {
        return historialEstados;
    }

    public void setHistorialEstados(List<HistorialEstadoPropiedad> historialEstados) {
        this.historialEstados = historialEstados;
    }

	public EstadoPropiedad getEstado() {
		return estado;
	}

	public void setEstado(EstadoPropiedad estado) {
		this.estado = estado;
	}

	public Boolean getEliminado() {
		return eliminado;
	}

	public void setEliminado(Boolean eliminado) {
		this.eliminado = eliminado;
	}

    
}
