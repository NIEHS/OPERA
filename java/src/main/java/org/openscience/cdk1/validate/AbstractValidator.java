/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 * 
 * Copyright (C) 2003-2007  The Chemistry Development Kit (CDK) project
 * 
 * Contact: cdk-devel@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA. 
 * 
 */
package org.openscience.cdk1.validate;

import org.openscience.cdk1.interfaces.IAtom;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.interfaces.IAtomType;
import org.openscience.cdk1.interfaces.IBond;
import org.openscience.cdk1.interfaces.IChemFile;
import org.openscience.cdk1.interfaces.IChemModel;
import org.openscience.cdk1.interfaces.IChemObject;
import org.openscience.cdk1.interfaces.IChemSequence;
import org.openscience.cdk1.interfaces.ICrystal;
import org.openscience.cdk1.interfaces.IElectronContainer;
import org.openscience.cdk1.interfaces.IElement;
import org.openscience.cdk1.interfaces.IIsotope;
import org.openscience.cdk1.interfaces.IMolecule;
import org.openscience.cdk1.interfaces.IReaction;
import org.openscience.cdk1.interfaces.IMoleculeSet;
import org.openscience.cdk1.interfaces.IReactionSet;

/**
 * Abstract validator that does nothing but provide all the methods that the
 * ValidatorInterface requires.
 *
 * @cdk.module extra
 * @cdk.githash
 *
 * @author   Egon Willighagen
 * @cdk.created  2004-03-27
 * @cdk.require java1.4+
 */ 
public class AbstractValidator implements IValidator {

    public ValidationReport validateChemObject(IChemObject subject) {
        ValidationReport report = new ValidationReport();
        return report;
    }
    public ValidationReport validateAtom(IAtom subject) {
        ValidationReport report = new ValidationReport();
        return report;
    }
    public ValidationReport validateAtomContainer(IAtomContainer subject) {
        ValidationReport report = new ValidationReport();
        return report;
    }
    public ValidationReport validateAtomType(IAtomType subject) {
        ValidationReport report = new ValidationReport();
        return report;
    }
    public ValidationReport validateBond(IBond subject) {
        ValidationReport report = new ValidationReport();
        return report;
    }
    public ValidationReport validateChemFile(IChemFile subject) {
        ValidationReport report = new ValidationReport();
        return report;
    }
    public ValidationReport validateChemModel(IChemModel subject) {
        ValidationReport report = new ValidationReport();
        return report;
    }
    public ValidationReport validateChemSequence(IChemSequence subject) {
        ValidationReport report = new ValidationReport();
        return report;
    }
    public ValidationReport validateCrystal(ICrystal subject) {
        ValidationReport report = new ValidationReport();
        return report;
    }
    public ValidationReport validateElectronContainer(IElectronContainer subject) {
        ValidationReport report = new ValidationReport();
        return report;
    }
    public ValidationReport validateElement(IElement subject) {
        ValidationReport report = new ValidationReport();
        return report;
    }
    public ValidationReport validateIsotope(IIsotope subject) {
        ValidationReport report = new ValidationReport();
        return report;
    }
    public ValidationReport validateMolecule(IMolecule subject) {
        ValidationReport report = new ValidationReport();
        return report;
    }
    public ValidationReport validateReaction(IReaction subject) {
        ValidationReport report = new ValidationReport();
        return report;
    }
    public ValidationReport validateMoleculeSet(IMoleculeSet subject) {
        ValidationReport report = new ValidationReport();
        return report;
    }
    public ValidationReport validateReactionSet(IReactionSet subject) {
        ValidationReport report = new ValidationReport();
        return report;
    }
    
}
