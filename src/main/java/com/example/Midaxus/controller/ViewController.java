package com.example.midaxus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for managing web views.
 */
@Controller
public class ViewController {

  /**
   * Renders the index page.
   *
   * @return the name of the index template
   */
  @GetMapping("/")
  public String index() {
    return "index";
  }

  /**
   * Renders the login page.
   *
   * @return the name of the login template
   */
  @GetMapping("/login")
  public String login() {
    return "login";
  }


  /**
   * Renders the dashboard page.
   *
   * @return the name of the dashboard template
   */
  @GetMapping("/dashboard")
  public String dashboard() {
    return "dashboard";
  }

  /**
   * Renders the forgot password page.
   *
   * @return the name of the forgotpassword template
   */
  @GetMapping("/forgotpassword")
  public String forgotpassword() {
    return "forgotpassword";
  }

  /**
   * Renders the reset password page.
   *
   * @return the name of the reset-password template
   */
  @GetMapping("/reset-password")
  public String resetPassword() {
    return "reset-password";
  }
}

