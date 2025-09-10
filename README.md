# Yocto
Yocto-based project builds a Linux image that includes a Qt application and the vsomeip middleware, with an organized folder structure for custom layers and recipes

# meta-PAO Layer Documentation

## Folder Structure

```
meta-PAO/
├── .vscode/
│   └── settings.json                    # VSCode workspace configuration
├── conf/
│   ├── layer.conf                       # Layer metadata and dependencies
│   └── distro/
│       └── pao-distro.conf              # Custom distribution configuration
├── recipes-core/
│   ├── images/
│   │   └── pao-image.bb                 # Main system image recipe
│   └── psplash/
│       ├── psplash_%.bbappend           # Boot splash customization
│       └── files/
│           └── psplash-pao-img.h        # Custom splash screen image data
├── recipes-graphics/
│   └── weston/
│       ├── weston-init.bbappend         # Wayland compositor configuration
│       └── weston-init/
│           └── weston.ini               # Weston runtime settings
├── recipes-qt/
│   └── pao/
│       ├── pao.bb                       # Qt application build recipe
│       ├── pao.bbappend                 # Additional application configuration
│       └── files/
│           ├── can.sh                   # CAN bus initialization script
│           ├── disable-serial-getty.sh  # Serial port management script
│           ├── ip.sh                    # Network configuration script
│           └── pao.service              # Systemd service definition
└── recipes-vsomeip/
    └── vsomeip/
        └── vsomeip_3.5.5.bb            # Automotive communication library
```

## Overview

The `meta-PAO` layer is a custom Yocto/OpenEmbedded meta-layer designed to build an embedded Linux distribution for automotive/IoT applications. It integrates Qt6 applications with automotive communication protocols (vSomeIP, CAN) and provides a complete embedded system image for Raspberry Pi platforms.

## Layer Architecture

### Configuration Files

#### `conf/layer.conf`
**Purpose**: Meta-layer configuration and dependency declaration
- **Role**: Defines the layer structure, build file patterns, and dependencies
- **Key Functions**:
  - Sets `BBPATH` to include configuration and classes directories
  - Defines `BBFILES` pattern to include all `.bb` and `.bbappend` files
  - Establishes layer priority (`BBFILE_PRIORITY_meta-PAO = "6"`)
  - Declares dependencies on core layers: `core`, `qt6-layer`, `openembedded-layer`, `meta-python`, `raspberrypi`
  - Sets compatibility with Kirkstone release series

#### `conf/distro/pao-distro.conf`
**Purpose**: Custom distribution configuration for PAO embedded system
- **Role**: Defines the overall system characteristics and feature set
- **Key Functions**:
  - Sets distribution name, version, and maintainer information
  - Configures systemd as the init system (`VIRTUAL-RUNTIME_init_manager = "systemd"`)
  - Enables Wayland and Vulkan graphics features
  - Defines supported host development systems
  - Inherits security flags and build optimizations from Poky

### Image Definition

#### `recipes-core/images/pao-image.bb`
**Purpose**: Complete system image recipe that defines what gets installed
- **Role**: Main entry point for building the complete embedded system
- **Key Components**:
  - Base image type: `core-image` (provides minimal Linux system)
  - Graphics: Weston compositor with Wayland support
  - Connectivity: SSH access via Dropbear, network management with ConnMan
  - CAN support: Kernel modules and utilities for automotive communication
  - Qt application: PAO cluster dashboard application
  - vSomeIP: Automotive service communication middleware
- **Relationships**: Dependencies on all other recipes in the layer

### Boot Experience Customization

#### `recipes-core/psplash/psplash_%.bbappend`
**Purpose**: Customizes the boot splash screen
- **Role**: Overrides default psplash configuration to show PAO branding
- **Mechanism**: Points to custom splash image header file
- **Related Files**: Works with `psplash-pao-img.h` to display custom boot graphics

#### `recipes-core/psplash/files/psplash-pao-img.h`
**Purpose**: Contains custom boot splash image as C array data
- **Role**: Provides the actual image data displayed during system boot
- **Format**: Run-length encoded RGBA pixel data (972x972 resolution)
- **Integration**: Compiled into psplash binary during build

### Qt Application Integration

#### `recipes-qt/pao/pao.bb`
**Purpose**: Main Qt6 application recipe for the PAO cluster dashboard
- **Role**: Builds and packages the automotive cluster application
- **Key Features**:
  - Qt6 with Wayland support for modern graphics
  - Serial communication for automotive protocols
  - vSomeIP integration for service communication
  - CMake-based build system
- **Source**: Fetches code from GitHub repository
- **Dependencies**: Qt6 base, serial port, Wayland, and boost libraries

#### `recipes-qt/pao/pao.bbappend`
**Purpose**: Additional configuration and files for the PAO application
- **Role**: Extends the base recipe with system integration files
- **Added Files**: System configuration scripts and service definitions

### System Integration Scripts

#### `recipes-qt/pao/files/pao.service`
**Purpose**: Systemd service definition for automatic application startup
- **Role**: Ensures PAO application starts automatically after boot
- **Dependencies**: Requires Weston compositor to be running first
- **Environment**: Sets up Wayland display environment variables
- **Startup Sequence**:
  1. Disables serial getty (frees up serial ports)
  2. Configures network interface
  3. Initializes CAN bus
  4. Waits 10 seconds for system stabilization
  5. Starts the PAO cluster application
- **Fault Tolerance**: Automatic restart on failure

#### `recipes-qt/pao/files/ip.sh`
**Purpose**: Network interface configuration script
- **Role**: Sets up static IP configuration for the embedded system
- **Function**: Configures eth0 with IP address 192.168.2.2/24
- **Integration**: Called by pao.service during startup

#### `recipes-qt/pao/files/can.sh`
**Purpose**: CAN bus initialization script
- **Role**: Configures CAN interface for automotive communication
- **Integration**: Called by pao.service, allows failure without stopping boot

#### `recipes-qt/pao/files/disable-serial-getty.sh`
**Purpose**: Serial port management script
- **Role**: Disables serial console getty to free up UART for application use
- **Necessity**: Required for applications that need direct serial port access

### Graphics and Display

#### `recipes-graphics/weston/weston-init.bbappend`
**Purpose**: Customizes Weston compositor configuration
- **Role**: Extends base Weston setup with PAO-specific settings
- **Integration**: Adds custom weston.ini configuration file

#### `recipes-graphics/weston/weston-init/weston.ini`
**Purpose**: Weston compositor runtime configuration
- **Role**: Configures graphics compositor behavior
- **Settings**: Disables screen idle timeout (keeps display always on)
- **Purpose**: Ensures automotive dashboard remains visible

### Communication Middleware

#### `recipes-vsomeip/vsomeip/vsomeip_3.5.5.bb`
**Purpose**: Builds vSomeIP automotive communication library
- **Role**: Provides SOME/IP protocol implementation for automotive services
- **Version**: Fixed at 3.5.5 for stability and certification requirements
- **Dependencies**: Boost libraries for C++ functionality
- **Configuration**: Enables signal handling and sets default config path
- **Integration**: Required by PAO application for vehicle communication

## System Integration Flow

### Boot Sequence
1. **Hardware Initialization**: Raspberry Pi boot process
2. **Kernel Loading**: Linux kernel with CAN and graphics support
3. **Boot Splash**: Custom PAO splash screen via psplash
4. **System Services**: systemd starts core services
5. **Graphics**: Weston compositor initializes Wayland display
6. **Network Setup**: ip.sh configures ethernet interface
7. **CAN Initialization**: can.sh sets up automotive bus
8. **Application Launch**: PAO Qt application starts on Wayland

### Inter-Component Relationships
- **pao-image.bb** → coordinates all components into bootable system
- **pao.service** → orchestrates startup sequence and dependencies
- **weston-init** → provides graphics foundation for Qt application
- **vsomeip** → enables automotive protocol communication
- **Network/CAN scripts** → prepare communication interfaces
- **psplash customization** → provides branded boot experience

## Development Workflow

### Layer Dependencies
The meta-PAO layer requires these layers in your `bblayers.conf`:
- `meta` (OE-Core)
- `meta-poky` (Poky reference distribution)
- `meta-yocto-bsp` (BSP support)
- `meta-openembedded/meta-oe` (Additional recipes)
- `meta-openembedded/meta-python` (Python support)
- `meta-raspberrypi` (Raspberry Pi BSP)
- `meta-qt6` (Qt6 framework)

### Build Configuration
- **Target Machine**: Typically `raspberrypi3-64` or `raspberrypi4-64`
- **Distribution**: `pao-distro` (defined in this layer)
- **Main Target**: `pao-image` produces complete SD card image

### Customization Points
- **Application Source**: Modify `SRC_URI` in `pao.bb` for different code branches
- **Network Configuration**: Edit `ip.sh` for different IP settings
- **Startup Behavior**: Modify `pao.service` for different boot sequence
- **Graphics Settings**: Adjust `weston.ini` for display configuration
- **Boot Branding**: Replace `psplash-pao-img.h` with custom splash image

## Quality Assurance

### System Validation
- **Boot Testing**: Verify complete boot sequence from splash to application
- **Network Connectivity**: Confirm ethernet configuration and connectivity
- **CAN Communication**: Test automotive protocol functionality
- **Graphics Performance**: Validate Wayland/Qt rendering performance
- **Service Management**: Ensure proper startup/restart behavior

### Development Tools
- **VSCode Integration**: `.vscode/settings.json` provides development environment setup
- **Build Verification**: All recipes include proper dependencies and file packaging
- **Runtime Validation**: Service files include restart policies and error handling