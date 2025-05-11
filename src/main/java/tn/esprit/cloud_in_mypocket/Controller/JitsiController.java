package tn.esprit.cloud_in_mypocket.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.cloud_in_mypocket.Config.JitsiProperties;
import tn.esprit.cloud_in_mypocket.service.JitsiService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/jitsi")
public class JitsiController {
  @Autowired 
  private JitsiService jitsi;
  
  @Autowired
  private JitsiProperties jitsiProps;

  @GetMapping("/token")
  public Map<String,Object> getToken(
      @RequestParam String room,
      @RequestParam String userId,
      @RequestParam String displayName) {

    Map<String,Object> resp = new HashMap<>();
    resp.put("host", jitsiProps.getHost());
    resp.put("room", room);
    // only generate token if appSecret is set
    if (jitsiProps.getAppSecret() != null && !jitsiProps.getAppSecret().isEmpty()) {
      String token = jitsi.generateJwt(room, userId, displayName);
      resp.put("token", token);
    }
    return resp;
  }
}
