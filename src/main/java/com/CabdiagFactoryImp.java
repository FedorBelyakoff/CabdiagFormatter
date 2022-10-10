package com;

import com.interfaces.CabdiagFactory;

public class CabdiagFactoryImp implements CabdiagFactory {
    @Override
    public Cabdiag cabdiagOf(String buffered) {
        return new Cabdiag(buffered);
    }
}
