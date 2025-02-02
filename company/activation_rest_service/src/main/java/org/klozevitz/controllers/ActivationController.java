package org.klozevitz.controllers;

import lombok.RequiredArgsConstructor;
import org.klozevitz.services.implementations.ActivatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class ActivationController {
    private final ActivatorService activatorService;

    @RequestMapping(method = RequestMethod.GET, value = "/activation")
    public ResponseEntity<?> activation(@RequestParam("id") String id) {
        return activatorService.activate(id) ?
                ResponseEntity.ok("Регистрация завершена!") :
                ResponseEntity.internalServerError().build();
    }
}
