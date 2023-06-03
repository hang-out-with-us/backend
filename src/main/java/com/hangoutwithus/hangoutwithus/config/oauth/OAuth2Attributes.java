package com.hangoutwithus.hangoutwithus.config.oauth;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Getter
@Builder
//분기처리를 위한 클래스
public class OAuth2Attributes {
    private Map<String,Object> attributes;
    private String attributeKey;
    private String email;
    private String name;

    public static OAuth2Attributes of(String provider, String attributeKey, Map<String,Object> attributes) {
        switch(provider){
            case "google":
                return ofGoogle(attributeKey,attributes);
//            case "naver":
//                return ofNaver("id",attributes);
//            case "kakao":
//                return ofKakao("id",attributes);
            default:
                throw new IllegalArgumentException("Unknown provider: " + provider);
        }
    }

    private static OAuth2Attributes ofGoogle(String attributeKey, Map<String,Object> attributes) {

        return OAuth2Attributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }

    public Map<String,Object> convertToMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",attributeKey);
        map.put("attributeKey",attributeKey);
        map.put("name",name);
        map.put("email",email);

        return map;
    }


}
