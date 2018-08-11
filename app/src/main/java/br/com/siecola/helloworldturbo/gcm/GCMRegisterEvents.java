package br.com.siecola.helloworldturbo.gcm;

import java.io.IOException;

public interface GCMRegisterEvents {

    void gcmRegisterFinished (String registrationID);

    void gcmRegisterFailed (IOException ex);

    void gcmUnregisterFinished ();

    void gcmUnregisterFailed (IOException ex);
}