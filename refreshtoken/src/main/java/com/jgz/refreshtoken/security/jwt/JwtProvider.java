package com.jgz.refreshtoken.security.jwt;

import com.jgz.refreshtoken.security.dto.JwtDto;
import com.jgz.refreshtoken.security.entity.UsuarioPrincipal;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    //loger para poder cachar y visiualozar ls errores en la consaola
    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

//aqui llamo las varibles de mi properties donde defini
    //CAVE RECALCAR LA CLAVESECRETA ESTA EN MD5 DEBE ESTAR ENCRIPTADA POR QUE SI NO MARCA ERROR AL HACER LOGIN
//la clave secreta sin encriptar en md5 es:superadmin
// la clave secreta encriptada en md5 es:17c4520f6cfd1ab53d8745e84681eb49
    @Value("${jwt.secret}")
    private String claveSecret;
    @Value("${jwt.expiration}")
    private  Integer expiration;

    /*ARMANDO 03/12/2022
    * ESTA FUNCION GENERA EL TOKEN AL HACER
    * LOGIN O AUTHENTICARSE*/
    public String generateToken(Authentication authentication){
        UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();
        List<String> roles = usuarioPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        //return Jwts.builder().setSubject(usuarioPrincipal.getUsername());
        //AQUI AL EXPIRATION LE QUITE EL MULTIPLICADO * 1000 PARA QUE APLIQUE LOS 20 SEGUNDOS, .setExpiration(new Date(new Date().getTime() + expiration * 1000))
        return Jwts.builder().setSubject(usuarioPrincipal.getEmail())
                .claim("roles", roles)
                .setExpiration(new Date(new Date().getTime() + expiration))
                .signWith(Keys.hmacShaKeyFor(claveSecret.getBytes()))
                .compact();
    }

    /*public String getNameUserFromToken(String token){
        return null;
    }*/

    /*ARMANDO 03/12/2022
    * ESTA FUNCION OBTENEMOS EL EMAIL O USER NAME */
    public String getEmailUserFromToken(String token){
        //esta linea esta comentada por que esta deprecada se sustituyo por la de mas abajito
        //return Jwts.parser().setSigningKey(claveSecret.getBytes()).parseClaimsJwt(token).getBody().getSubject();
        return Jwts.parserBuilder().setSigningKey(claveSecret.getBytes()).build().parseClaimsJwt(token).getBody().getSubject();
    }

    /*ARMANDO 03/12/2022
    * ESTA FUNCION VALIDA EL TOKEN
    * SI ESTA CORRECTO Y CONTIENE
    * LA CLAVE SECRETA O SI ESTE
    * NO ESTA FORMADO CORRECTAMENTE*/
    public boolean validateToken(String token){
        try{
            //esta linea esta comentada por que esta deprecada se sustituyo por la de mas abajito
            //Jwts.parser().setSigningKey(claveSecret.getBytes()).parseClaimsJwt(token);
            Jwts.parserBuilder().setSigningKey(claveSecret.getBytes()).build().parseClaimsJwt(token);
            return true;
        }catch (MalformedJwtException e){
            logger.error("Token mal formado");
        }catch (UnsupportedJwtException e){
            logger.error("Token No Soportado");
        }catch (ExpiredJwtException e){
            logger.error("Token Expirado");
        }catch (IllegalArgumentException e){
            logger.error("Token Vacio");
        }catch (SignatureException e){
            logger.error("Token Fail en la firma");
        }
        return false;
    }

    /*EN EL REFRSH TOKEN ES EL MISMO PROCESO QUE CUANDO SE CREA
    * SOLO SE CAMBIAN ALGUNAS COSAS SE IMPLEMENTO UNA LIBRERIA DE
    * nimbus jose
    *
    * EN EL CATCH DICE QUE SI EL TOKEN EXPIRO ENTONCES LO REFRESCA
    * EN EL TRY ES PARA QUE AUN QUE LE PASES UN TOKEN QUE NO ESTE
    * FIRMADO CORRECTAMENTE NO LO DEJARA HACER NADA ESTO SI SUSTITUYEN
    * EL TOKEN DEL STORAGE DEL NAVEGADOR*/
    public String refreshToken(JwtDto jwtDto) throws ParseException {

        try{

            //esta linea esta comentada por que esta deprecada se sustituyo por la de mas abajito
//            Jwts.parser().setSigningKey(claveSecret.getBytes()).parseClaimsJwt(jwtDto.getToken());
            Jwts.parserBuilder().setSigningKey(claveSecret.getBytes()).build().parseClaimsJwt(jwtDto.getToken());
            System.out.println("EL TOKEN ES VALIDO AUN NO HA CADUCADO");
            logger.info("EL TOKEN ES VALIDO AUN NO HA CADUCADO");

        }catch (ExpiredJwtException e){

            JWT jwt = JWTParser.parse(jwtDto.getToken());
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            String email = claims.getSubject();
            List<String> roles = (List<String>) claims.getClaim("roles");
//AQUI AL EXPIRATION LE QUITE EL MULTIPLICADO * 1000 PARA QUE APLIQUE LOS 20 SEGUNDOS, .setExpiration(new Date(new Date().getTime() + expiration * 1000))
            return Jwts.builder()
                    .setSubject(email)
                    .claim("roles", roles)
                    .setExpiration(new Date(new Date().getTime() + expiration ))
                    .signWith(Keys.hmacShaKeyFor(claveSecret.getBytes()))
                    .compact();

        }

        return null;

    }

}
