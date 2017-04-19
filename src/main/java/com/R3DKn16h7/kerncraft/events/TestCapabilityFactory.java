package com.R3DKn16h7.kerncraft.events;

import java.util.concurrent.Callable;

/**
 * Created by Filippo on 19-Apr-17.
 */
public class TestCapabilityFactory implements Callable<IExampleCapability> {

    @Override
    public IExampleCapability call() throws Exception {
        return new TestCabability();
    }

}
