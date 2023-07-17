# Currency Converter - Android Compose Multi-Module Project

Currency Converter is an Android project built using Jetpack Compose and follows a multi-module architecture. It provides the functionality to convert currency from one to another, with various modules handling different aspects of the application.

## Modules

The project is structured into the following modules:

1. **data**: Contains data-related classes and interfaces, such as repositories and data sources.
2. **database**: Handles database interactions, including Room database setup and DAOs.
3. **domain**: Contains domain-specific classes and business logic, including use cases and domain models.
4. **model**: Defines common models used across the application.
5. **network**: Handles network-related operations, including API clients and network data sources.
6. **feature/dashboard**: The main feature module that displays the currency conversion dashboard.

## Features

- Currency conversion: Convert currency from one to another with real-time exchange rates.
- Jetpack Compose UI: Utilize the Jetpack Compose toolkit to build a modern and reactive user interface.
- Multi-module architecture: Separate concerns into different modules for better code organization and maintainability.
- Data persistence: Store and retrieve currency data using Room database.
- Network calls: Fetch real-time exchange rates from an external API.

## Getting Started

To get started with the Currency Converter project, follow these steps:

1. Clone the repository:

   ```bash
   git clone https://github.com/ShushantTiwari-ashu/CurrencyConverter
   ```

2. Import the project into Android Studio.

3. Build and run the application.

## How to Use

The main feature module, `feature/dashboard`, is the entry point of the Currency Converter application. It allows users to input an amount in one currency and select another currency for conversion.

The currency conversion functionality is implemented using the data, domain, and network modules to fetch exchange rates and perform currency calculations.

## Architecture

The Currency Converter project follows a clean and modular architecture to ensure separation of concerns and maintainability. The modules are organized as follows:

- `data`: Handles data-related operations and provides data to other modules.
- `database`: Contains database-related classes and setup for local data persistence.
- `domain`: Contains the business logic and use cases for currency conversion.
- `model`: Defines common models shared across the application.
- `network`: Handles network operations, including API calls to fetch exchange rates.
- `feature/dashboard`: The main feature module that showcases the currency conversion dashboard using Jetpack Compose.

## Dependencies

The Currency Converter project uses the following dependencies:

- Jetpack Compose: For building the modern UI.
- Room: For local database setup and data persistence.
- Retrofit: For making network API calls.
- Coroutines: For asynchronous programming and concurrency handling.

## Testing

The `test` module contains unit tests for different modules to ensure code correctness and reliability.

## Contributing

Contributions to the Currency Converter project are welcome! If you find a bug, have an enhancement in mind, or want to add new features, please feel free to open an issue or submit a pull request.

## License

The Currency Converter project is licensed under the [MIT License](LICENSE). Feel free to use, modify, and distribute the code for personal and commercial projects.
