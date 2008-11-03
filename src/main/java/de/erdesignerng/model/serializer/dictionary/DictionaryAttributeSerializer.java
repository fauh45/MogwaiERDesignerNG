/**
 * Mogwai ERDesigner. Copyright (C) 2002 The Mogwai Project.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package de.erdesignerng.model.serializer.dictionary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import de.erdesignerng.model.Attribute;
import de.erdesignerng.model.Domain;
import de.erdesignerng.model.Table;
import de.erdesignerng.model.serializer.dictionary.entities.AttributeEntity;
import de.erdesignerng.model.serializer.dictionary.entities.TableEntity;

/**
 * Serializer for attributes.
 * 
 * @author msertic
 */
public class DictionaryAttributeSerializer extends DictionarySerializer {

    public static final DictionaryAttributeSerializer SERIALIZER = new DictionaryAttributeSerializer();
    
    protected void copyExtendedAttributes(Attribute aSource, AttributeEntity aDestination) {
        aDestination.setDatatype(null);
        aDestination.setDomain(null);
        if (!(aSource.getDatatype() instanceof Domain)) {
            aDestination.setDatatype(aSource.getDatatype().getName());
        } else {
            Domain theDomain = (Domain) aSource.getDatatype();
            aDestination.setDomain(theDomain.getSystemId());
        }
        aDestination.setSize(aSource.getSize());
        aDestination.setFraction(aSource.getFraction());
        aDestination.setScale(aSource.getScale());
        aDestination.setNullable(aSource.isNullable());
        aDestination.setDefaultValue(aSource.getDefaultValue());
        aDestination.setExtra(aSource.getExtra());
    }
    
    public void serialize(Table aTable, TableEntity aTableEntity, Session aSession) {

        Set<AttributeEntity> theRemovedAttributes = new HashSet<AttributeEntity>();
        
        Map<String, AttributeEntity> theAttributes = new HashMap<String, AttributeEntity>();
        for (AttributeEntity theAttributeEntity : aTableEntity.getAttributes()) {
            Attribute theAttribute = aTable.getAttributes().findBySystemId(theAttributeEntity.getSystemId());
            if (theAttribute == null) {
                theRemovedAttributes.add(theAttributeEntity);
            } else {
                theAttributes.put(theAttributeEntity.getSystemId(), theAttributeEntity);
            }
        }
        
        aTableEntity.getAttributes().removeAll(theRemovedAttributes);
        
        for (Attribute theAttribute : aTable.getAttributes()) {
            boolean existing = true;
            AttributeEntity theEntity = theAttributes.get(theAttribute.getSystemId());
            if (theEntity == null) {
                theEntity = new AttributeEntity();
                existing = false;
            }
            
            copyBaseAttributes(theAttribute, theEntity);
            copyExtendedAttributes(theAttribute, theEntity);
            
            if (existing) {
                aSession.update(theEntity);
            } else {
                aTableEntity.getAttributes().add(theEntity);
                aSession.save(theEntity);
            }
        }
        
    }
}