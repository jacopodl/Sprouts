/*
    MIT License

    Copyright (c) 2017 Jacopo De Luca

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
*/

package sprouts.exceptions;

import java.lang.reflect.*;

/***
 * This exception is thrown by injector to indicate access violation when trying to inject private/protected field/method or constructor.
 */
public class SproutsAccessViolation extends RuntimeException {
    public SproutsAccessViolation(Class clazz, AccessibleObject aobj) {
        super(new BuildInfo().getErrorMsg(clazz, aobj));
    }

    private static class BuildInfo {
        private String getErrorMsg(Class clazz, AccessibleObject aobj) {
            String type;

            if (aobj instanceof Field)
                type = String.format("field(%s)", ((Field) aobj).getName());
            else if (aobj instanceof Constructor)
                type = String.format("constructor(%s)", ((Constructor) aobj).getName());
            else
                type = String.format("method(%s)", ((Method) aobj).getName());

            return String.format("Unable to inject dependency into %s %s in class %s", getModifier(aobj), type, clazz.getCanonicalName());
        }

        private String getModifier(AccessibleObject aobj) {
            Member member = (Member) aobj;

            if (Modifier.isPrivate(member.getModifiers()))
                return "private";
            else if (Modifier.isProtected(member.getModifiers()))
                return "protected";
            return "";
        }
    }
}
