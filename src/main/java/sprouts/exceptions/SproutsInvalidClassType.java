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

import java.lang.reflect.Modifier;

/***
 * This exception is thrown by injector when trying instantiate interface or abstract class.
 */
public class SproutsInvalidClassType extends RuntimeException {
    public SproutsInvalidClassType(Class clazz) {
        super(new BuildInfo().getErrorMsg(clazz));
    }

    private static class BuildInfo {
        private String getErrorMsg(Class clazz) {
            String type = "";

            if (Modifier.isAbstract(clazz.getModifiers()))
                type = "abstract";
            if (Modifier.isInterface(clazz.getModifiers()))
                type = "interface";

            return String.format("Unable to build object from class %s because this class is %s.", clazz.getCanonicalName(), type);
        }
    }
}
