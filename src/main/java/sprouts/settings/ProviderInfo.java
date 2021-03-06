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

package sprouts.settings;

public class ProviderInfo {
    private Class baseClass;
    private Class qualifier;
    private boolean override;

    public ProviderInfo(Class base) {
        this.baseClass = base;
        this.qualifier = null;
        this.override = false;
    }

    /***
     * Set the destination of binding.
     * @param qualifier Concrete class returned by injector
     * @return {@link sprouts.settings.ProviderInfo}
     */
    public ProviderInfo to(Class qualifier) {
        this.qualifier = qualifier;
        return this;
    }

    /***
     * Ignore {@literal @}Bind annotation and use this binding for create an object.
     */
    public void withOverride() {
        this.override = true;
    }

    public Class getBaseClass() {
        return this.baseClass;
    }

    public Class getQualifier() {
        return this.qualifier;
    }

    public boolean getOverride() {
        return this.override;
    }
}
