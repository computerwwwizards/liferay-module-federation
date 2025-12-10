# Project Architecture - Liferay Module Federation

## Table of Contents
1. [System Overview](#system-overview)
2. [System Architecture](#system-architecture)
3. [Build Process](#build-process)
4. [Deploy Process](#deploy-process)
5. [Runtime Process](#runtime-process)
6. [Complete Request Flow](#complete-request-flow)

---

## System Overview

This project is a **Liferay Workspace** that uses **Module Federation** to create modular React portlets that run on Liferay DXP (Digital Experience Platform).

### Main Components
- **Liferay DXP**: Java-based digital experience platform
- **OSGi Runtime**: Dynamic module system for Java
- **Portlets**: Independent modules that can be added/removed at runtime
- **Module Federation**: Technology for sharing JavaScript code between applications

---

## System Architecture

### 1. Technology Stack

```
┌─────────────────────────────────────────┐
│           CLIENT (Browser)               │
│  - Rendered HTML                         │
│  - JavaScript (React)                    │
│  - CSS                                   │
└──────────────┬──────────────────────────┘
               │ HTTP/HTTPS
               ▼
┌─────────────────────────────────────────┐
│      HTTP SERVER (Tomcat)                │
│  - Port 8080 (default)                   │
│  - Serves static content                 │
│  - Proxy for Java applications           │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│    JAVA VIRTUAL MACHINE (JVM)           │
│  - Runtime for Java code                 │
│  - Memory management (Heap, Stack)       │
│  - Garbage Collection                    │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│         LIFERAY DXP PLATFORM            │
│  ┌─────────────────────────────────┐   │
│  │    OSGi CONTAINER               │   │
│  │  - Dynamic module system        │   │
│  │  - Load/unload at runtime       │   │
│  │  - Dependency management        │   │
│  └─────────────────────────────────┘   │
│  ┌─────────────────────────────────┐   │
│  │    PORTLET REGISTRY             │   │
│  │  - Registry of active portlets  │   │
│  │  - Route resolution             │   │
│  │  - Permission control           │   │
│  └─────────────────────────────────┘   │
└─────────────────────────────────────────┘
```

### 2. System Components

#### 2.1 Client (Frontend)
- **Web Browser**: Renders HTML, CSS and executes JavaScript
- **React Application**: Compiled application loaded dynamically
- **Module Federation**: Allows loading remote modules at runtime

#### 2.2 HTTP Server (Tomcat)
- **Port**: 8080 (configurable)
- **Function**: 
  - Receives HTTP/HTTPS requests
  - Serves static files (CSS, JS, images)
  - Delegates dynamic requests to JVM

#### 2.3 Java Virtual Machine (JVM)
- **Version**: Java 8 (according to project)
- **Function**:
  - Executes Java bytecode
  - Manages memory and resources
  - Provides runtime for Liferay

#### 2.4 OSGi Container
**OSGi (Open Service Gateway Initiative)** is a system that enables:
- **Modularity**: Each portlet is an independent module
- **Dynamic loading**: Add/remove modules without restarting the server
- **Lifecycle management**: Activate, deactivate, update modules
- **Isolation**: Each module has its own ClassLoader
- **Versioning**: Multiple versions of the same module can coexist

#### 2.5 Portlets (OSGi Modules)
A **Portlet** is a reusable web component that:
- Is packaged as a **JAR** file
- Contains:
  - **Compiled Java code** (bytecode in `/META-INF`)
  - **Static files** (JS, CSS, HTML)
  - **JSP resources** for server-side rendering
  - **OSGi configuration** (bnd.bnd, MANIFEST.MF)
  - **Declared dependencies**

**Structure of a portlet JAR:**
```
react_widget.jar
├── META-INF/
│   ├── MANIFEST.MF           # OSGi metadata
│   └── resources/
│       ├── view.jsp           # JSP template
│       ├── init.jsp           # Initialization
│       └── js/
│           └── index.js       # Compiled React app
├── react_widget/
│   ├── portlet/
│   │   └── React_widgetPortlet.class  # Java bytecode
│   └── constants/
│       └── React_widgetPortletKeys.class
└── content/
    └── Language.properties    # i18n
```

---

## Build Process

### 1. Command: `./gradlew build`

**Gradle** is a build automation tool for Java projects that:
- Manages dependencies
- Executes compilation tasks
- Packages artifacts
- Runs tests

### 2. Build Process Flow

```
┌──────────────────────────────────────────────────┐
│ 1. DEPENDENCY ANALYSIS                           │
│    - Reads build.gradle                          │
│    - Downloads necessary libraries               │
│    - Resolves version conflicts                  │
└────────────────┬─────────────────────────────────┘
                 │
                 ▼
┌──────────────────────────────────────────────────┐
│ 2. FRONTEND COMPILATION (Rsbuild)                │
│    - Executes: npm run build                     │
│    - Processes TypeScript → JavaScript           │
│    - Compiles React components                   │
│    - Applies Module Federation config            │
│    - Output: build/resources/main/META-INF/      │
│              resources/js/index.js                │
└────────────────┬─────────────────────────────────┘
                 │
                 ▼
┌──────────────────────────────────────────────────┐
│ 3. JAVA COMPILATION                              │
│    - Compiles .java files → .class               │
│    - Processes OSGi annotations                  │
│    - Validates @Component annotations            │
│    - Output: build/classes/                      │
└────────────────┬─────────────────────────────────┘
                 │
                 ▼
┌──────────────────────────────────────────────────┐
│ 4. RESOURCE PROCESSING                           │
│    - Copies JSP files                            │
│    - Processes properties files                  │
│    - Includes static assets                      │
│    - Output: build/resources/                    │
└────────────────┬─────────────────────────────────┘
                 │
                 ▼
┌──────────────────────────────────────────────────┐
│ 5. OSGi MANIFEST GENERATION (bnd.bnd)            │
│    - Reads bnd.bnd configuration                 │
│    - Generates MANIFEST.MF with OSGi metadata    │
│    - Defines:                                    │
│      * Bundle-Name: react_widget                 │
│      * Bundle-SymbolicName: react_widget         │
│      * Bundle-Version: 1.0.3                     │
│      * Web-ContextPath: /react_widget            │
│      * Import-Package: dependencies              │
│      * Export-Package: exposed APIs              │
└────────────────┬─────────────────────────────────┘
                 │
                 ▼
┌──────────────────────────────────────────────────┐
│ 6. JAR PACKAGING                                 │
│    - Creates .jar file                           │
│    - Includes:                                   │
│      * Compiled classes (.class)                 │
│      * Static resources (JS, CSS)                │
│      * JSP files                                 │
│      * MANIFEST.MF                               │
│    - Output: build/libs/react_widget.jar         │
└──────────────────────────────────────────────────┘
```

### 3. Key Files in Build

| File | Purpose |
|---------|-----------|
| `build.gradle` | Defines tasks, dependencies and project configuration |
| `bnd.bnd` | OSGi bundle configuration (module metadata) |
| `rsbuild.config.ts` | Frontend bundler configuration (Rsbuild) |
| `module-federation.config.ts` | Module Federation configuration |
| `package.json` | npm dependencies and frontend scripts |

---

## Deploy Process

### 1. Command: `./gradlew deploy`

This command performs two main actions:

```
┌──────────────────────────────────────────────────┐
│ 1. BUILD                                         │
│    - Executes ./gradlew build                    │
│    - Generates updated JAR                       │
└────────────────┬─────────────────────────────────┘
                 │
                 ▼
┌──────────────────────────────────────────────────┐
│ 2. COPY TO DEPLOY DIRECTORY                      │
│    - Source: build/libs/react_widget.jar         │
│    - Destination: /opt/liferay/deploy/           │
│                   (or bundles/deploy/ locally)   │
└────────────────┬─────────────────────────────────┘
                 │
                 ▼
┌──────────────────────────────────────────────────┐
│ 3. HOT DEPLOY (Automatic)                        │
│    - Liferay Auto-Deploy Listener detects JAR    │
│    - Validates format and OSGi metadata          │
│    - Installs bundle in OSGi container           │
│    - Activates the bundle                        │
│    - Registers portlet in Portlet Registry       │
│    - Status: ACTIVE                              │
└──────────────────────────────────────────────────┘
```

### 2. Hot Deploy in Detail

**Liferay Auto-Deploy** is a component that:

1. **Monitors** the `/opt/liferay/deploy/` directory
2. **Detects** new JAR files
3. **Validates** that they are valid OSGi bundles
4. **Installs** the bundle in the OSGi container
5. **Resolves** dependencies automatically
6. **Activates** the bundle (changes state to ACTIVE)
7. **Registers** the portlet in the system

**OSGi Bundle States:**
```
INSTALLED → RESOLVED → STARTING → ACTIVE
```

### 3. Deploy Verification

Deploy was successful when:
- ✅ JAR disappears from `/deploy/` (moved to `/osgi/modules/`)
- ✅ Appears in logs: `STARTED react_widget_1.0.3`
- ✅ Portlet appears in Liferay's widget menu
- ✅ Static resources are accessible at `/o/react_widget/js/`

---

## Runtime Process

### 1. System Initialization

```
┌──────────────────────────────────────────────────┐
│ 1. LIFERAY STARTUP                               │
│    - Starts Tomcat HTTP Server                   │
│    - Initializes JVM                             │
│    - Loads OSGi Framework                        │
└────────────────┬─────────────────────────────────┘
                 │
                 ▼
┌──────────────────────────────────────────────────┐
│ 2. LOADING OSGi BUNDLES                          │
│    - Scans /osgi/modules/                        │
│    - Installs found bundles                      │
│    - Resolves dependencies between bundles       │
│    - Activates bundles in correct order          │
└────────────────┬─────────────────────────────────┘
                 │
                 ▼
┌──────────────────────────────────────────────────┐
│ 3. PORTLET REGISTRATION                          │
│    - Reads @Component annotation in Java classes │
│    - Registers portlet in Portlet Registry       │
│    - Associates routes and resources             │
│    - Configures permissions and categories       │
└────────────────┬─────────────────────────────────┘
                 │
                 ▼
┌──────────────────────────────────────────────────┐
│ 4. SERVER READY                                  │
│    - Tomcat listening on port 8080               │
│    - Portlets available for rendering            │
│    - Static assets being served                  │
└──────────────────────────────────────────────────┘
```

### 2. Portlet Architecture at Runtime

```
┌─────────────────────────────────────────────────────┐
│              PORTLET: react_widget                  │
├─────────────────────────────────────────────────────┤
│                                                     │
│  ┌───────────────────────────────────────────┐    │
│  │  JAVA COMPONENT (Server-Side)             │    │
│  │  ────────────────────────────────────     │    │
│  │  React_widgetPortlet.java                 │    │
│  │  - Extends MVCPortlet                     │    │
│  │  - Handles portlet lifecycle              │    │
│  │  - Processes requests/responses           │    │
│  │  - Renders view.jsp                       │    │
│  └───────────────────────────────────────────┘    │
│                       │                             │
│                       ▼                             │
│  ┌───────────────────────────────────────────┐    │
│  │  JSP TEMPLATE (Server-Side Rendering)     │    │
│  │  ────────────────────────────────────     │    │
│  │  view.jsp                                 │    │
│  │  - Generates initial HTML                 │    │
│  │  - Creates <div id="namespace-root">      │    │
│  │  - Injects <script> to load React         │    │
│  │  - Uses Liferay taglibs                   │    │
│  └───────────────────────────────────────────┘    │
│                       │                             │
│                       ▼                             │
│  ┌───────────────────────────────────────────┐    │
│  │  REACT APPLICATION (Client-Side)          │    │
│  │  ────────────────────────────────────     │    │
│  │  /o/react_widget/js/index.js              │    │
│  │  - Dynamically imported in client         │    │
│  │  - Mounts to the root div                 │    │
│  │  - Renders interactive UI                 │    │
│  │  - Module Federation enabled              │    │
│  └───────────────────────────────────────────┘    │
│                                                     │
└─────────────────────────────────────────────────────┘
```

---

## Complete Request Flow

### Scenario: User loads a page with the `react_widget` portlet

```
┌──────────────────────────────────────────────────────────────┐
│ STEP 1: INITIAL REQUEST                                      │
└──────────────────────────────────────────────────────────────┘

[Client] ────────► [Tomcat:8080]
          GET /web/guest/home
          
          User navigates to a Liferay page


┌──────────────────────────────────────────────────────────────┐
│ STEP 2: SERVER-SIDE PROCESSING                               │
└──────────────────────────────────────────────────────────────┘

[Tomcat] ────────► [Liferay Runtime]
                   
                   1. Liferay identifies the requested page
                   2. Queries which portlets are on that page
                   3. Finds: react_widget portlet


[Liferay Runtime] ────────► [Portlet Registry]
                            
                            1. Searches for bundle: react_widget
                            2. Gets class: React_widgetPortlet
                            3. Verifies user permissions


[Portlet Registry] ────────► [React_widgetPortlet Instance]
                             
                             1. Invokes render() method
                             2. Loads view.jsp
                             3. Processes JSP tags


[view.jsp] ────────► [Generated HTML]

<%@ include file="/init.jsp" %>
<div id="<portlet:namespace />-root"></div>
<aui:script>
    import(
        Liferay.ThemeDisplay.getPathContext() + 
        '/o/react_widget/js/index.js'
    ).then(
        (module) => module.default('<portlet:namespace />-root')
    );
</aui:script>

                             ↓ Generates ↓

<div id="myportlet_WAR_react_widget-root"></div>
<script type="module">
    import('/o/react_widget/js/index.js')
        .then(module => 
            module.default('myportlet_WAR_react_widget-root')
        );
</script>


┌──────────────────────────────────────────────────────────────┐
│ STEP 3: RESPONSE TO CLIENT                                   │
└──────────────────────────────────────────────────────────────┘

[Liferay] ────────► [Client]
          200 OK
          Content-Type: text/html
          
          <html>
            <body>
              <!-- Other Liferay components -->
              
              <div id="myportlet_WAR_react_widget-root"></div>
              <script type="module">
                import('/o/react_widget/js/index.js')...
              </script>
              
            </body>
          </html>


┌──────────────────────────────────────────────────────────────┐
│ STEP 4: ASSET LOADING (Client-Side)                          │
└──────────────────────────────────────────────────────────────┘

[Client/Browser] ────────► [Tomcat:8080]
                  GET /o/react_widget/js/index.js
                  
                  Browser detects dynamic import
                  and requests the JavaScript file


[Tomcat] ────────► [OSGi Resource Handler]
                   
                   1. Identifies bundle: react_widget
                   2. Path: /o/react_widget → Web-ContextPath
                   3. Searches: META-INF/resources/js/index.js
                   4. Reads file from JAR


[OSGi] ────────► [Client]
       200 OK
       Content-Type: application/javascript
       
       export default function(rootId) {
           // Compiled React code
           ReactDOM.render(<App />, 
               document.getElementById(rootId)
           );
       }


┌──────────────────────────────────────────────────────────────┐
│ STEP 5: REACT RENDERING (Client-Side)                        │
└──────────────────────────────────────────────────────────────┘

[JavaScript Engine] ────────► [Code Execution]
                              
                              1. Executes index.js
                              2. Calls init(rootId)
                              3. Mounts React app to DOM
                              4. Renders components

[React] ────────► [DOM]
                  
                  Updates the <div id="...root">
                  with the rendered React application


┌──────────────────────────────────────────────────────────────┐
│ FINAL RESULT                                                 │
└──────────────────────────────────────────────────────────────┘

User sees:
✅ Rendered Liferay page (Server-Side)
✅ Integrated react_widget portlet
✅ Interactive React application (Client-Side)
✅ Module Federation ready to consume remotes
```

---

## Complete Sequence Diagram

```
Client         Tomcat      Liferay    OSGi       Portlet     JSP         React
  │               │           │         │           │          │            │
  ├──GET /home───>│           │         │           │          │            │
  │               ├──────────>│         │           │          │            │
  │               │           ├────────>│           │          │            │
  │               │           │         ├──find────>│          │            │
  │               │           │         │<──found───┤          │            │
  │               │           │         ├─render()──>│         │            │
  │               │           │         │           ├─load────>│            │
  │               │           │         │           │<─HTML────┤            │
  │               │           │<─HTML───┤           │          │            │
  │               │<─HTML─────┤         │           │          │            │
  │<──200 OK──────┤           │         │           │          │            │
  │               │           │         │           │          │            │
  ├──GET index.js─>│           │         │           │          │            │
  │               ├──────────────────────>│           │          │            │
  │               │<─────────────────────┤           │          │            │
  │<──JS file─────┤           │         │           │          │            │
  │               │           │         │           │          │            │
  ├─execute JS────────────────────────────────────────────────>│            │
  │               │           │         │           │          │            │
  │<─React UI rendered──────────────────────────────────────────┤            │
  │               │           │         │           │          │            │
```

---

## Key Concepts Summary

### OSGi Container
- Dynamic module system for Java
- Enables hot-deploy (add/remove modules without restart)
- Manages bundle lifecycle
- Isolates dependencies between modules

### Portlet (OSGi Bundle)
- Module packaged as JAR
- Contains Java code + static resources
- Automatically registered in Liferay
- Can have multiple instances on a page

### Module Federation
- Webpack/Rsbuild technology
- Enables sharing JavaScript code between apps
- Loads remote modules at runtime
- Avoids dependency duplication (React, React-DOM)

### Hybrid Flow (Server + Client)
1. **Server-Side**: Liferay renders JSP → initial HTML
2. **Client-Side**: Browser loads React → interactive application
3. **Hydration**: React takes control of the DOM
4. **SPA Behavior**: Interactions without page reload

---

## Glossary

| Term | Definition |
|---------|------------|
| **Portlet** | Modular web component that can be added to Liferay pages |
| **Bundle** | OSGi module packaged as JAR with special metadata |
| **OSGi** | Dynamic modularity framework for Java |
| **Hot Deploy** | Ability to install modules without restarting the server |
| **JAR** | Java Archive - compressed file with Java code and resources |
| **JSP** | JavaServer Pages - template for generating HTML on server |
| **MVCPortlet** | Liferay base class for creating portlets with MVC pattern |
| **Namespace** | Unique prefix to avoid ID collisions in the DOM |
| **Web-ContextPath** | Base path for serving static resources from a bundle |
| **Module Federation** | Architecture for sharing code between JavaScript apps |

---

## References

- [Liferay DXP Documentation](https://learn.liferay.com/dxp/latest/en/index.html)
- [OSGi Alliance](https://www.osgi.org/)
- [Module Federation](https://module-federation.io/)
- [Gradle Build Tool](https://gradle.org/)
