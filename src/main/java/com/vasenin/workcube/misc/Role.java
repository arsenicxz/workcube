package com.vasenin.workcube.misc;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
   USER, ADMIN;

   @Override
   public String getAuthority() {
      return "ROLE_" + name(); // добавляем префикс ROLE_ для совместимости с Spring Security
   }
}
