package net.ttcxy.chat.security;

import net.ttcxy.chat.api.ApiException;
import net.ttcxy.chat.entity.CurrentMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUtil {

   private static final Logger LOG = LoggerFactory.getLogger(CurrentUtil.class);

   private CurrentUtil() {
   }

   /**
    * 获取当前登录的用户名
    */
   public static String name() {
      CurrentMember currentMember = member();
      return currentMember.getUsername();
   }

   /**
    * 获取当前登录的用户名
    */
   public static CurrentMember member() {

      final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null) {
         throw new ApiException("未登录");
      }

      return (CurrentMember) authentication.getPrincipal();
   }
}
