package com.ppe.TennisAcademy.config;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.ppe.TennisAcademy.entities.User;
import com.ppe.TennisAcademy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class VerificationTokenService {

    @Autowired
    UserService userService;
    private static final Integer EXPIRE_MINS = 1440;

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    private LoadingCache<String, String> tokenCache;

    public VerificationTokenService(){
        super();
        tokenCache = CacheBuilder.newBuilder().
                expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES).build(new CacheLoader<String, String>() {
            public String load(String key) {
                return "";
            }
        });
    }


    //This method is used to push the opt number against Key. Rewrite the verificationToken if it exists
    //Using user id  as key
    public String generateverificationToken(String key){

        Random random = new Random();
        String verificationToken = randomString(20);
        tokenCache.put(key, verificationToken);
        return  verificationToken;
    }

    //This method is used to return the OPT number against Key->Key values is username
    public String getverificationToken(String key){
        try{
            return tokenCache.get(key);
        }catch (Exception e){
            return "";
        }
    }

    //This method is used to clear the verificationToken catched already
    public void clearverificationToken(String key){
        tokenCache.invalidate(key);
    }


    public void verifyverificationToken(Long userId,  String verificationToken) {

        User user = userService.getByID(userId);
        if (getverificationToken(userId+"").equals(verificationToken)) {
            user.setVerified(true);

             userService.save(user);
        }

    }

}
