package com.R3DKn16h7.kerncraft.capabilities.manual;

import java.util.concurrent.Callable;

/**
 * Created by Filippo on 19-Apr-17.
 */
public class TyrociniumProgressFactory implements Callable<ITyrociniumProgressCapability> {

    @Override
    public ITyrociniumProgressCapability call() throws Exception {
        return new TyrociniumProgressDefaultCapability();
    }

}
