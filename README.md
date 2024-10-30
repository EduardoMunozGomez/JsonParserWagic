# Wagic JSON Parser Tool

This tool automates the generation of essential Wagic game files by parsing JSON data sourced from [MTGJSON](https://mtgjson.com/downloads/all-sets/). It creates the `_cards.dat` files, image links, and a structured outline for primitive abilities, enabling streamlined setup and expansion of Wagic’s card library.

## Features

- **Card Data Generation**: Transforms MTGJSON data into `_cards.dat` files, defining each card’s attributes and abilities.
- **Image Link Creation**: Produces links for card images, simplifying artwork integration.
- **Primitive Outline Support**: Generates a base outline for Wagic’s primitives, aiding in the addition of frequently used abilities or effects.

## Getting Started

### Requirements

- **Java Development Kit (JDK)**: Required to compile and run the tool.
- **MTGJSON Data File**: The tool processes a JSON file from MTGJSON containing detailed card information, including names, types, abilities, etc.

### Installation

1. **Clone or Download the Repository**: Retrieve the tool’s source files.
2. **Download JSON Data**: Obtain the latest JSON file from [MTGJSON](https://mtgjson.com/), formatted with card details.
3. **Compile the Tool**: Run `javac JsonParserWagic.java` to compile the parser.

### Usage

1. **Run the Tool**: Execute the main class, passing the MTGJSON file as input. The tool will output three primary components:
    - `_cards.dat` file(s): Formatted card data for Wagic.
    - Image Links JSON: A file containing URLs for card images.
    - Primitive Outline: A reference list of common abilities and effects for Wagic’s core files.
    
2. **Output Files**:
    - **_cards.dat**: Contains individual card definitions, including attributes like name, type, cost, and abilities.
    - **Image Links**: Lists image URLs for each card, making artwork integration straightforward.
    - **Primitives Outline**: Provides a list of common abilities, enhancing the flexibility and efficiency of card updates.
