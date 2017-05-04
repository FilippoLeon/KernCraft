package com.R3DKn16h7.kerncraft.capabilities.element;

import java.util.concurrent.Callable;

/**
 * Created by Filippo on 19-Apr-17.
 */
public class ElementContainerFactory
        implements Callable<IElementContainer> {

    @Override
    public IElementContainer call() throws Exception {
        return new ElementContainerDefaultCapability();
    }
}
