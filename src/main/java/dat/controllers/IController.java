package dat.controllers;

import dat.exceptions.ApiException;

import io.javalin.http.Context;
import java.util.List;

public interface IController <T>{
    void read(Context ctx) throws ApiException;
    void readAll(Context ctx)throws ApiException;
    void create(Context ctx)throws ApiException;
    void update(Context ctx)throws ApiException;
    void delete(Context ctx)throws ApiException;

}
