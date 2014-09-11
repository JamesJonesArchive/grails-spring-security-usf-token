/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.usf.cims.token

import org.springframework.security.core.AuthenticationException
/**
 *
 * @author james
 */
class UsfTokenAuthenticationException extends AuthenticationException {
    UsfTokenAuthenticationException(String msg) {
        super(msg)
    }
}

