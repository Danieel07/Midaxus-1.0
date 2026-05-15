---
name: Midaxus
description: Una plataforma de gestión académica estructurada y moderna.
colors:
  primary: "#3b9a95"
  primary-dark: "#2d7a73"
  primary-light: "#e6f3fc"
  secondary: "#1a3a52"
  neutral-bg: "#f8fafb"
  neutral-surface: "#ffffff"
  border: "#d4e3f0"
  text-muted: "#7e9eb5"
  danger: "#930606"
typography:
  display:
    fontFamily: "Inter, system-ui, sans-serif"
    fontSize: "2rem"
    fontWeight: 700
    lineHeight: 1.2
  body:
    fontFamily: "Inter, system-ui, sans-serif"
    fontSize: "0.875rem"
    fontWeight: 400
    lineHeight: 1.5
rounded:
  sm: "4px"
  md: "8px"
  lg: "12px"
spacing:
  xs: "4px"
  sm: "8px"
  md: "16px"
  lg: "24px"
components:
  button-primary:
    backgroundColor: "{colors.primary}"
    textColor: "#ffffff"
    rounded: "{rounded.md}"
    padding: "8px 16px"
  card:
    backgroundColor: "{colors.neutral-surface}"
    rounded: "{rounded.md}"
    padding: "24px"
---

# Design System: Midaxus

## 1. Overview

**Creative North Star: "The Academic Grid"**

Midaxus se basa en la precisión y la legibilidad. Como sistema de gestión académica, la jerarquía visual debe ser absoluta, permitiendo que los usuarios (estudiantes y profesores) procesen grandes cantidades de datos sin fatiga cognitiva. El sistema rechaza el exceso de decoración, prefiriendo la claridad estructural sobre las sombras pesadas y los gradientes.

**Key Characteristics:**
- Alta densidad de información sin desorden.
- Contraste tipográfico fuerte para guiar el ojo.
- Una paleta "Restrained" basada en verdes académicos y azules profundos.

## 2. Colors

La paleta se inspira en la seriedad institucional pero con un toque moderno y fresco.

### Primary
- **Deep Academic Teal** (#3b9a95): El color de acción principal. Transmite confianza y estabilidad.
- **Teal Light** (#e6f3fc): Utilizado para estados de hover y fondos de secciones activas.

### Secondary
- **Midnight Institutional Navy** (#1a3a52): Color de contraste para headers y texto de alta importancia.

### Neutral
- **Campus White** (#ffffff): Superficies de trabajo y tarjetas.
- **Fresh Campus Air** (#f8fafb): El fondo base de la aplicación.
- **Academic Border** (#d4e3f0): Delimitación sutil de secciones sin añadir peso visual.

### Named Rules
**The Rarity Rule.** El color teal se usa en menos del 10% de la pantalla. Su valor reside en su escasez para dirigir la atención a las acciones críticas.

## 3. Typography

**Font Family:** Inter (con fallbacks de sistema). Elegida por su excelente legibilidad en pantallas pequeñas y datos tabulares.

### Hierarchy
- **Display** (700, 2rem, 1.2): Títulos de página principales.
- **Headline** (600, 1.25rem, 1.3): Encabezados de sección.
- **Body** (400, 0.875rem, 1.5): Texto principal y datos. Máxima longitud de línea de 70ch.
- **Label** (700, 0.75rem, normal, uppercase): Metadatos, etiquetas de tabla y micro-copys.

## 4. Elevation

El sistema es mayoritariamente plano (flat) para mantener la limpieza visual. La profundidad se comunica a través del color de fondo y bordes sutiles en lugar de sombras complejas.

### Named Rules
**The Flat-By-Default Rule.** Las superficies son planas en estado de reposo. Solo se permiten sombras sutiles (0 1px 4px rgba(0,0,0,0.07)) para elevar elementos interactivos durante el hover o para "session cards" en el horario.

## 5. Components

### Buttons
- **Shape:** Bordes suavemente redondeados (8px).
- **Primary:** Fondo Teal (#3b9a95), texto blanco, padding responsivo.
- **Hover:** Transición suave a Teal Dark (#2d7a73).

### Cards
- **Style:** Fondo blanco, borde Academic Border (1px), sin sombra o sombra mínima.
- **Internal Padding:** 24px (1.5rem).

### Inputs
- **Style:** Borde gris claro, fondo blanco, foco con anillo Teal de 2px.

## 6. Do's and Don'ts

### Do:
- **Do** usar espaciado generoso entre secciones para permitir que el "grid" respire.
- **Do** usar la tipografía Label en mayúsculas para encabezados de tabla.

### Don't:
- **Don't** usar sombras pesadas o difusas que sugieran "flotación" excesiva.
- **Don't** usar gradientes de azul a púrpura ("AI slop").
- **Don't** usar "side-stripe borders" mayores a 4px como acento de color.
