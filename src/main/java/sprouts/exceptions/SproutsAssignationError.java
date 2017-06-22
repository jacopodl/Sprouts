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

/***
 * This exception is thrown by injector when trying to inject not compatible object.
 * <br>
 * <br>
 * Example:
 * <br>
 * class Alfa
 * <br>
 * class Beta extends Alfa
 * <br>
 * class Lambda
 * <br>
 * <br>
 * &#64;GetInstance
 * <br>
 * &#64;BindTo(className="Lambda")
 * <br>
 * Beta b;
 */
public class SproutsAssignationError extends RuntimeException {
    public SproutsAssignationError(Class base, Class clazz) {
        super(String.format("Unable to assign object! Object %s not extends object %s", clazz.getCanonicalName(), base.getCanonicalName()));
    }
}
