package com.example.pet_shelter.exceptions;

public class DogNullParameterValueException extends RuntimeException{
    public DogNullParameterValueException(String massage) {
        super(massage);
    }
}
