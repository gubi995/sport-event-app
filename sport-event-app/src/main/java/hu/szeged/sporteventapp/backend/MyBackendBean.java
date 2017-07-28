/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hu.szeged.sporteventapp.backend;

import org.springframework.stereotype.Service;

/**
 * Implementation of {@link org.vaadin.spring.samples.security.managed.backend.MyBackend}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@Service
public class MyBackendBean implements MyBackend {

    @Override
    public String adminOnlyEcho(String s) {
        return "admin:" + s;
    }

    @Override
    public String echo(String s) {
        return s;
    }
}
