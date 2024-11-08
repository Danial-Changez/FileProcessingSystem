# File Processing System

## Table of Contents
1. [Overview](#overview)
2. [Features](#features)
3. [Setup & Installation](#setup--installation)
4. [Dependencies](#dependencies)
5. [Configure JSON File](#configure-json-file)
6. [Usage](#usage)
   - [Run the Program](#run-the-program)
7. [Example JSON Configuration](#example-json-configuration)
8. [Error Handling](#error-handling)
9. [Available Processing Methods](#available-processing-methods)
10. [Filters Class](#filters-class)
11. [Print Method](#print-method)

## Overview

This project was designed in collaboration with my peers to handle file processing tasks, including filtering, renaming, counting, and splitting files based on various criteria. It supports local file systems and can be extended for remote repositories, such as Laserfiche.

## Features

- **File Filtering**: Filter files by name, length, content, or the number of occurrences of a keyword.
- **File Management**: Rename, split, or print file details.
- **Configurable Operations**: Apply various processing elements via JSON configuration files.
- **Directory and File Handling**: Works with both files and directories.

## Setup & Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Danial-Changez/FileProcessingSystem.git


2. **Open the project in NetBeans**:

- Launch NetBeans IDE.
- Go to **File > Open Project** and select the folder where you cloned the repository.

3. **Build and Run**:

- In NetBeans, right-click the project and select **Build** to compile the project.
- Once built, click **Run** to execute the program.

## Dependencies

- **Java 8 or higher**
- **Laserfiche API client** (for remote operations)
- **JSON.simple library** for parsing JSON configuration files

## Configure JSON File

The program uses a JSON file to configure the file operations (e.g., filtering and renaming).

## Usage

### Run the Program

Compile and run the Java project. The main program will process files based on the configured JSON file.

## Example JSON Configuration

The following JSON configuration can be used to filter files based on size and content. It processes files from a local directory, filtering those that are larger than 5000 bytes and those containing the keyword "important".

```json
{
  "processing_elements": [
    {
      "type": "Length",
      "input_entries": [
        {
          "type": "local",
          "path": "C:/path/to/directory"
        }
      ],
      "parameters": [
        {
          "name": "Length",
          "value": 5000
        },
        {
          "name": "Operator",
          "value": "GT"
        }
      ]
    },
    {
      "type": "Content",
      "input_entries": [
        {
          "type": "local",
          "path": "C:/path/to/file.txt"
        }
      ],
      "parameters": [
        {
          "name": "Key",
          "value": "important"
        }
      ]
    }
  ]
}
```
## Error Handling

The program includes error handling for common file-related issues:

- **Invalid file paths**: Ensure the file or directory paths are correct.
- **File not found**: Check if the files specified in the configuration exist.
- **IOException / FileNotFoundException**: These exceptions may be thrown if there are issues with reading or accessing files.
- **JSONException**: Occurs when there are errors in parsing the JSON configuration file.

Make sure to verify all paths and parameters to avoid these errors during processing.

## Available Processing Methods

- **Name**: Filters files by whether their names contain the specified key.
- **Length**: Filters files based on size, supporting operators like EQ, GT, LT, GTE, LTE, and NEQ.
- **Content**: Filters files that contain a specified keyword in their content.
- **List**: Returns a list of files from a directory, up to a specified maximum.
- **Count**: Filters files based on the occurrence of a keyword in their content.
- **Split**: Splits files into parts, each containing a specified number of lines.
- **Rename**: Renames files by appending a specified suffix to their names.

## `filters` Class

The `filters` class provides operations to filter and process files based on various criteria, such as name, length, content, list, count, split, and rename.

## `print` Method

The `print` method outputs details about files, including their name, size, and absolute path, for both local and remote entries.
