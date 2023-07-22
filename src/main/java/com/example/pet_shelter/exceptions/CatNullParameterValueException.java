package com.example.pet_shelter.exceptions;

public class CatNullParameterValueException extends RuntimeException{
    public CatNullParameterValueException(String massage) {
        super(massage);
    }
}
