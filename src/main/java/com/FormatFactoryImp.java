package com;

import com.interfaces.FormatFactory;

public class FormatFactoryImp implements FormatFactory {
    @Override
    public Format doubleState(Cabdiag stateOn, Cabdiag stateOff) {
        return Format.doubleState(stateOn, stateOff);
    }


    @Override
    public Format stateOn(Cabdiag in) {
        return Format.stateOn(in);
    }


    @Override
    public Format stateOff(Cabdiag in) {
        return Format.stateOff(in);
    }
}
