package com.supermap.datashare.core;

import java.io.Serializable;

/**
 * @author lifeng
 *
 */
public interface SuperModel<ID extends Serializable> {

    public abstract ID getId();

    public abstract void setId(ID id);

    public abstract void resetModifyState();

    public abstract String[] getIgnoreProperties();
}
