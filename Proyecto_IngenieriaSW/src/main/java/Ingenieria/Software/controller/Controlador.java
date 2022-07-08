package Ingenieria.Software.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import Ingenieria.Software.model.Departamento;
import Ingenieria.Software.model.Producto;
import Ingenieria.Software.model.Usuario;
import Ingenieria.Software.service.ServiceDepartamento;
import Ingenieria.Software.service.ServiceProducto;
import Ingenieria.Software.service.ServiceUsuario;

@Controller
public class Controlador {
	@Autowired
	ServiceUsuario serviceUsuario;
	
	@Autowired
	ServiceDepartamento serviceDepartamento;
	
	@Autowired
	ServiceProducto serviceProducto;
	
	
	//============================================================================================
	//Seguridad
	//============================================================================================
	@Autowired
	PasswordEncoder passwordEncoder;
	@RequestMapping(value="/encriptar"  ,method=RequestMethod.GET)
	public String encriptarContrasenia(){
		List<Usuario> lista = this.serviceUsuario.obtenerTodosUsuarios();
		String contrasenia;
		for (Usuario e: lista) {
			if(e.getContrasenia().length()<16) {
			contrasenia = passwordEncoder.encode(e.getContrasenia());
			System.out.print("heber"+"pasa a"+contrasenia);
			e.setContrasenia(contrasenia);
			this.serviceUsuario.crear(e);
			}
			
		}
		return"encriptar";
	}
	
	
	
	/***************************************************************USUARIO**********************************************************************/
	@GetMapping("/inicio")
	public String registrarCompania(){
		return "index";
	}
	@GetMapping("/")
	public String registrarCompani(){
		return "inicio";
	}
	
	
	@GetMapping("/terminos")
	public String terminos(){
		return "terminos";
	}
	
	@GetMapping("/login")
	public String login(){
		return "login";
	}
	
	
	
	
	
	
	
	
	
	
	
	//============================================================================================
	//Usuarios
	//============================================================================================
	
	@RequestMapping(value= "/usuarios/iniciarSesion",method=RequestMethod.POST)
	public String crearUsuario(
								  @RequestParam(name = "primerNombre") String primerNombre,
								  @RequestParam(name = "segundoNombre") String segundoNombre, 
								  @RequestParam(name = "primerApellido") String primerApellido,
								  @RequestParam(name = "segundoApellido") String segundoApellido,
								  @RequestParam(name = "correoElectronico") String correoElectronico,
								  @RequestParam(name = "contrasenia") String contrasenia,
								  @RequestParam(name = "idDepartamento") int idDepartamento,
								  @RequestParam(name = "telefono") int telefono,
								  @RequestParam(name = "direccion") String direccion,
								  @RequestParam(name = "suscripcion") String suscripcion){
		/*int id, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido,
			String correoElectronico, String direccion, String rol, int idDepartamento*/
		try {
			Usuario usuario= new Usuario(primerNombre,segundoNombre,primerApellido,segundoApellido,correoElectronico,contrasenia,idDepartamento,telefono,direccion,suscripcion);
			this.serviceUsuario.crear(usuario);
			return "login";
		}catch(Exception e) {
			return "/";
		}
	}

	
	
	/***************************************************************PRODUCTO**********************************************************************/

	@GetMapping("/productos/crearProductos")
    public String registrarProductos(){
        return "test2";
    }
	
	
	
	@GetMapping("/productos/busqueda")
    public String busquedaProductos(){
        return "busqueda";
    }
	
	@GetMapping("/productos/inicio")
    public String inicioProductos(){
        return "inicio";
    }
	
	@GetMapping("/productos/producto")
    public String productoProductos(){
        return "producto";
    }
	
	@GetMapping("/productos/autenticacion")
    public String autenticacionProductos(){
        return "autenticacion";
    }
	
	@GetMapping("/busqueda")
    public String busqueda(){
        return "busqueda";
    }
	

    @RequestMapping(value= "/productos/ingresoProductos",method=RequestMethod.POST)
    public String crearUsuario(
                                  @RequestParam(name = "nombre") String nombre,
                                  @RequestParam(name = "precio") int precio, 
                                  @RequestParam(name = "descripcion") String descripcion,
                                  @RequestParam(name = "fechaIngreso") @DateTimeFormat(iso = ISO.DATE) LocalDate fechaIngreso ,
                                  @RequestParam(name = "file") MultipartFile fotografias,
                                  @RequestParam(name = "idCategoria") int idCategoria,
                                  @RequestParam(name = "idUsuario") int idUsuario,
                                  @RequestParam(name = "idEstadoProducto") int idEstadoProducto){
        /*String nombre, int precio, String descripcion, Date fechaIngreso,
            byte[] fotografias, int idCategoria, int idUsuario, String idEstadoProducto*/
    	if(!fotografias.isEmpty()) {
    		Path directorioImagenes=Paths.get("src//main//resources//static/imgt");
    		String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
    		try {
    			byte[] bystesImg = fotografias.getBytes();
    			Path rutaCompleta = Paths.get(rutaAbsoluta+"//"+fotografias.getOriginalFilename());
    			Files.write(rutaCompleta, bystesImg);
    		}catch(IOException e) {
    			e.printStackTrace();
    		}
    		
    		
    	}

        try {
            Producto producto= new Producto(nombre,precio,descripcion,fechaIngreso,fotografias.getOriginalFilename(),idCategoria,idUsuario,idEstadoProducto);
            this.serviceProducto.crearProducto(producto);
            return "login";
        }catch(Exception e) {
            return "/";
        }

    }
/******USARRRRRRRR ESSSSSSSSTEEEEEEEEEEEEEEEEEEEEEE*****/
    @RequestMapping(value= "/producto/listarProductos",method=RequestMethod.GET)
    public String listaProducto( Model model){
        List<Producto> productos = this.serviceProducto.obtenerTodosProductos();
        model.addAttribute("producto", productos);
        return "inicio";
    }
    
    @RequestMapping(value= "/productos/listarProducto",method=RequestMethod.GET)
    public String listarProducto( Model model){
        List<Producto> productos = this.serviceProducto.obtenerTodosProductos();
        model.addAttribute("producto", productos);
        return "autenticacion";
    }
 
    
    //============================================================================================
    //Categorias
    //============================================================================================


  @GetMapping("/categorias/seleccionarCategorias")
  public String seleccionarCategorias(){
      return "seleccion";
  }
  
  @RequestMapping(value= "/categorias/seleccionarCategoria",method=RequestMethod.POST)
	public String seleccionarCategoria(
								  @RequestParam(name = "correoElectronico") String correoElectronico,
								  @RequestParam(name = "suscripcion") String suscripcion){
		/*int id, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido,
			String correoElectronico, String direccion, String rol, int idDepartamento*/
		try {
			List<Usuario> usuarios=this.serviceUsuario.obtenerTodosUsuarios();
			String c =correoElectronico;
			String ss= suscripcion;
			for (Usuario e: usuarios) {
				String s= e.getCorreoElectronico();
				if(e.getCorreoElectronico().equals(c)) {
					e.setSuscripcion(ss);
					this.serviceUsuario.crear(e);
				}
			}
			return "seleccion";
		}catch(Exception e) {
			return "/";
		}
	}
  	
  @GetMapping("/user")
  public String mostrarUsuario(Authentication auth){
	  String userName =auth.getName();
	  System.out.println("Usuario:"+userName);
      return "userName";
  }
  
  public String nP= "";
  @PostMapping(value = "/agregar/listaDeseo")
  public String agregarAlCarrito(Authentication auth, @RequestParam(name = "idProducto") String idProducto) {
	  String userName =auth.getName();  
	  
	  try {
			List<Usuario> usuarios=this.serviceUsuario.obtenerTodosUsuarios();
			for (Usuario e: usuarios) {
				if(e.getListaDeDeseos().equals("")) {
					nP = idProducto ;
				}else {
					nP = e.getListaDeDeseos() + ','+ idProducto ;
				}
				
				String s= e.getCorreoElectronico();
				if(e.getCorreoElectronico().equals(userName)) {
					e.setListaDeDeseos(nP);
					this.serviceUsuario.crear(e);
				}
			}
			return"exito";
		}catch(Exception e) {
			return"login";
		}
  }
  


}




















