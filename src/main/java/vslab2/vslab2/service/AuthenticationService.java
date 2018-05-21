package vslab2.vslab2.service;

import org.springframework.beans.factory.annotation.Autowired;
import vslab2.vslab2.config.AuthProperties;
import vslab2.vslab2.dbLayer.BitterDB;

public class AuthenticationService {
    @Autowired
    private BitterDB dao;

    @Autowired
    private AuthProperties authProps;


}
