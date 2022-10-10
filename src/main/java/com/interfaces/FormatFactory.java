package com.interfaces;

import com.Cabdiag;
import com.Format;

public interface FormatFactory {
    Format doubleState(Cabdiag stateOn, Cabdiag stateOff);

    Format stateOn(Cabdiag in);

    Format stateOff(Cabdiag in);
}
