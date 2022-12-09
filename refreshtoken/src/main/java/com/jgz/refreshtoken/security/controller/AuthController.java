package com.jgz.refreshtoken.security.controller;

import com.jgz.refreshtoken.security.dto.JwtDto;
import com.jgz.refreshtoken.security.dto.LoginUsuario;
import com.jgz.refreshtoken.security.dto.MensajeCustom;
import com.jgz.refreshtoken.security.dto.NuevoUsuario;
import com.jgz.refreshtoken.security.entity.Rol;
import com.jgz.refreshtoken.security.entity.Usuario;
import com.jgz.refreshtoken.security.enums.RolNombre;
import com.jgz.refreshtoken.security.jwt.JwtProvider;
import com.jgz.refreshtoken.security.service.RolService;
import com.jgz.refreshtoken.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolService rolService;

    @Autowired
    JwtProvider jwtProvider;

    // ver si aplico los cambios: https://www.youtube.com/watch?v=IC7qBKW92ic&list=PL4bT56Uw3S4z9rtwwGvuk1Mjhu5sdLSwX&index=15&ab_channel=LuigiCode

    /*ARMANDO 29/11/2022
    * FUNCION DONDE SE GENERA UN NUEVO USUARIO
    * JUNTO CON SUS ROLES*/
    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo( @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult){

        /*EN MI MODELO NUEVO USUARIO PONER ETIQUETAS QUE NO PUEDEN SER VACIOS O NULLOS LOS CAMPOS*/

        //ESTAS VALIDACIONES SON POR QUE EL USERNAME Y EL EMAIL SON UNICOS
        if (bindingResult.hasErrors())
            return  new ResponseEntity(new MensajeCustom("Campos Mal Puestos ó Email Inválido"), HttpStatus.BAD_REQUEST);

        //if (usuarioService.existsByNameUser()) lo cambiaria por este si quiero buscar por Username
        if (usuarioService.existsByEmail(nuevoUsuario.getEmail()))
            return new ResponseEntity(new MensajeCustom("Este Email ya Existe."), HttpStatus.BAD_REQUEST);

        if (usuarioService.existsByNameUser(nuevoUsuario.getNameUser()))
            return new ResponseEntity(new MensajeCustom("Este Nombre de Usuario ya Existe."), HttpStatus.BAD_REQUEST);

        Usuario usuario =
                new Usuario(nuevoUsuario.getName(), nuevoUsuario.getNameUser(), nuevoUsuario.getCreacionDate(), nuevoUsuario.getEmail(),
                        passwordEncoder.encode(nuevoUsuario.getPassword()));

        Set<Rol> roles = new HashSet<>();
        //poner un if para que asi cuando se agrege un usuario con pérmisos de admin no se agrege el de user tambien
        roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());

        if (nuevoUsuario.getRoles().contains("admin"))
            roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
        usuario.setCreacionDate(new Date());//aqui se agrega que se creo el usuario
        usuario.setRoles(roles);
        usuarioService.save(usuario);
        return new ResponseEntity(new MensajeCustom("Usuario Guardado con Exito"), HttpStatus.CREATED);

    }


    /*ARMANDO 29/11/2022
    * FUNCION DONDE SE HACE EL LOGIN DE LA SESION
    * Y DEVUELVE EL TOKEN DE JWT*/
    //hay error al hacer logn https://www.youtube.com/watch?v=zMoWEZaTQvs&list=PL4bT56Uw3S4z9rtwwGvuk1Mjhu5sdLSwX&index=7&ab_channel=LuigiCode
    //@PreAuthorize("hasRole('ADMIN')")//CON ESTA ETIQUETA INDICAMOS QUE SI TIENES EL ROL ADMIN ENTONCES PUEDES HACER USO DEL ENDPOINT SI NO SIMPLEMENTE ARROJA ERROR
    //@PostMapping(value="/login", consumes = {"application/json"})
    @PostMapping("/login")
    public ResponseEntity<JwtDto> login (@RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return  new ResponseEntity(new MensajeCustom("Email Inválido ó Contraseña"), HttpStatus.BAD_REQUEST);

        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getEmail(),loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);//aqui se genera el token

        Usuario userTemp = usuarioService.getByEmail(loginUsuario.getEmail()).get();//le paso mi id de usuario a mi dto jwt
        System.out.println(userTemp.getId());

        /*PODEMOS CREAR MODELO PARA OBTENER LA INFORMACION DEL ID ROL Y ASI
        * PODER MOSTRAR LAS ACCIONES QUE TIENE PERMITIDA AL IGUAL QUE PODRIAMOS
        * AGREGAR POR PLATA FORMAS DIFERENTES OSEA UN PLATAFORMA DE CLIENTES SI ES QE HAY
        * Y UNA DE PLATAFORMA ADMINISTRATIVA , PERO SI SOLO ES PARA USO INTENERTO VASTA CON LA
        * TABLA DE ROLES DONDE SE TIENEN LAS ACCIONES QUE TIENEN PERMITIDAS*/

        /*PRIMERO CONSULTARIA CON EL id de usuario A TABLA usuario_rol OBTENDRIA EL id rol
        * ASI OBTENDRIA TAMBIEN LOS PERMISOS DE CADA MODULO EN TEORIA */

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        //aqui es donde mando mi dto del token al front cuando se autentica correcftamente
        JwtDto jwtDto = new JwtDto (jwt, userDetails.getUsername(), userTemp.getId(), userDetails.getAuthorities());
        return new ResponseEntity(jwtDto, HttpStatus.OK);

    }

    /*NOTA FUNCIONA EL REFRESH TOKEN EL PROBLEMA ES QE NO DEVO ENVIAR EL ARREGLO DE AUTHORITIES
    * AL REFRESH POR QUE MARCA ERROR asi que posiblemente debo decodificarlo del token y despues
    * consultar los roles y las acciones*/
    @PostMapping("refresh")
    public ResponseEntity<JwtDto> refreshToken(@RequestBody JwtDto jwtDto) throws ParseException {

        String token = jwtProvider.refreshToken(jwtDto);
        JwtDto jwtDto1 = new JwtDto(token,"",null,new ArrayList<>());
        //AQUI PUEDO MANDAR POR EL SET LOS VALORES RESTANTANTES CONSULTANDO DESDE AQUI LOS
        //ENDPOINT O SIMPLEMENTE TOMAR LOS DATOS QUE SE ENVIAN PARA REFRESCART EL TOKEN OSEA jwtDto

        return new ResponseEntity(jwtDto1, HttpStatus.OK);

    }


}
