package org.openscience.cdk1.smsd.labelling;

import org.openscience.cdk1.interfaces.IReaction;

/**
 * @cdk.module smsd
 * @cdk.githash
 */

public class SmilesReactionCanoniser 
    extends AbstractReactionLabeller implements ICanonicalReactionLabeller {
    
    private CanonicalLabellingAdaptor labeller = new CanonicalLabellingAdaptor();

	/**
	 * {@inheritDoc}
	 */
    public IReaction getCanonicalReaction(IReaction reaction) {
        return super.labelReaction(reaction, labeller); 
    }
}
