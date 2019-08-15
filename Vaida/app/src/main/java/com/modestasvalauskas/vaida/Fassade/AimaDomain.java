package com.modestasvalauskas.vaida.Fassade;

import aima.core.logic.fol.StandardizeApartIndexicalFactory;
import aima.core.logic.fol.domain.FOLDomain;

/**
 * Created by Modestas Valauskas on 13.09.2016.
 */
public class AimaDomain {

    private FOLDomain domain;

    public AimaDomain() {
        StandardizeApartIndexicalFactory.flush();
        domain = new FOLDomain();
    }

    public FOLDomain getDomain() {
        return domain;
    }

}
