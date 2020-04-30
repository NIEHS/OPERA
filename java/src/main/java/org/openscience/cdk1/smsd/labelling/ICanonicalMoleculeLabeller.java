package org.openscience.cdk1.smsd.labelling;

import org.openscience.cdk1.interfaces.IAtomContainer;

/**
 * @cdk.module smsd
 * @cdk.githash
 */

public interface ICanonicalMoleculeLabeller {
    
    public IAtomContainer getCanonicalMolecule(IAtomContainer container);

    public int[] getCanonicalPermutation(IAtomContainer container);
}
