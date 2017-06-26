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

import java.util.HashMap;
import java.util.Map;

public abstract class SproutsSettings {
    /***
     * Allow injections on private field.
     */
    protected boolean allowPrivateField;

    /***
     * Allow injections on private constructor.
     */
    protected boolean allowPrivateConstructor;

    /***
     * Allow injections on private method.
     */
    protected boolean allowPrivateMethod;

    private Map<String, ProviderInfo> providerMap;

    public SproutsSettings() {
        this.providerMap = new HashMap<>();
        this.allowPrivateField = false;
        this.allowPrivateConstructor = false;
        this.allowPrivateMethod = false;
    }

    public ProviderInfo getMapping(Class base) {
        return this.providerMap.get(base.getName());
    }

    /***
     * Installs a new binding for create an object.
     * @param clazz Base class or interface
     * @return {@link sprouts.settings.ProviderInfo}
     */
    public ProviderInfo bind(Class clazz) {
        ProviderInfo pInfo = new ProviderInfo(clazz);
        this.providerMap.put(clazz.getName(), pInfo);
        return pInfo;
    }

    public boolean getAllowPrivateField() {
        return this.allowPrivateField;
    }

    public boolean getAllowPrivateConstructor() {
        return this.allowPrivateConstructor;
    }

    public boolean getAllowPrivateMethod() {
        return this.allowPrivateMethod;
    }

    /***
     * Create a new configuration for injector.
     * <br>
     * <b>Don't call this method!</b>
     */
    public abstract void configure();
}
