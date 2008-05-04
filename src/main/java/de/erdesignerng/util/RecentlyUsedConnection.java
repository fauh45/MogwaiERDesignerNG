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
package de.erdesignerng.util;

/**
 * An entry for the last used connection history.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-05-04 17:24:43 $
 */
public class RecentlyUsedConnection {

    private String dialect;

    private String url;

    private String username;

    public RecentlyUsedConnection(String aDialect, String aURL, String aUserName) {
        dialect = aDialect;
        url = aURL;
        username = aUserName;
    }

    /**
     * @return the dialect
     */
    public String getDialect() {
        return dialect;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((dialect == null) ? 0 : dialect.hashCode());
        result = PRIME * result + ((url == null) ? 0 : url.hashCode());
        result = PRIME * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RecentlyUsedConnection other = (RecentlyUsedConnection) obj;
        if (dialect == null) {
            if (other.dialect != null) {
                return false;
            }
        } else if (!dialect.equals(other.dialect)) {
            return false;
        }
        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!url.equals(other.url)) {
            return false;
        }
        if (username == null) {
            if (other.username != null) {
                return false;
            }
        } else if (!username.equals(other.username)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return dialect + "/" + username + " " + url;
    }
}