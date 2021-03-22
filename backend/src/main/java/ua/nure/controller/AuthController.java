package ua.nure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nure.dto.FloristShopDto;
import ua.nure.dto.auth.LoginDto;
import ua.nure.dto.auth.TokenDto;
import ua.nure.entity.User;
import ua.nure.security.JwtTokenUtil;
import ua.nure.security.UserDetailsImpl;
import ua.nure.service.FlowerService;
import ua.nure.service.FloristShopService;

import javax.validation.Valid;

import static ua.nure.validation.BindingResultValidator.errorBody;

@RestController
@CrossOrigin
@RequestMapping(path = "/auth")
@Api(tags = "Authorization")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final FloristShopService floristShopService;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
            FloristShopService floristShopService
    ) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.floristShopService = floristShopService;
    }

    @PostMapping("/login")
    @ApiOperation(value = "Performs user login to the system", nickname = "loginUser")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getEmail());
        final String token = new JwtTokenUtil().generateToken(userDetails);

        User user = ((UserDetailsImpl) userDetails).getUser();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new TokenDto()
                        .setToken(token)
                        .setEmail(user.getEmail())
                        .setUserRole(user.getRole().getName())
                );
    }

    @PostMapping("/register/florist-shop")
    @ApiOperation(value = "Registers a florist shop", nickname = "registerFloristShop")
    public ResponseEntity<?> registerFloristShop(
            @Valid @RequestBody FloristShopDto floristShopDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }

        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(floristShopService.create(floristShopDto));
    }

}
