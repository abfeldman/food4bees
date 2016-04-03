package nl.food4bees.backend;

import java.math.BigInteger;

import java.security.SecureRandom;

public class TokenGenerator
{
    private static SecureRandom random = new SecureRandom();

    public static String getToken()
    {
        return new BigInteger(130, random).toString(32);
    }
}
