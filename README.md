# TSP - Traveling Salesman Problem Solver (Java)

## Overview 🔍

This Java-based project aims to solve the **Traveling Salesman Problem (TSP)** using a parallelized approach with multithreading and the Partially Mapped Crossover (PMX) technique. The TSP is a classic optimization problem where the goal is to find the most efficient route that visits a set of cities exactly once and returns to the starting city.

---

## Fork objectives 🚀

The main goal of this fork is to adapt the original project to bea able to generate reports for a performance analysis between parallel capabilities in different programming languages. The main goal is to compare the performance of the Java version with the Python version.

---

## How to run? 🏃

To run the project, you need to have Java SDK 21 installed on your machine. You can download it [here](https://www.oracle.com/java/technologies/downloads/#java21).
After that, you can run the main class ```Main.java```.

---

## Features ✅

### 1 - Multithreading

The solution utilizes multithreading to concurrently explore different paths in the solution space, improving the overall efficiency of the algorithm. This enables the program to explore multiple potential solutions simultaneously, leading to faster convergence towards an optimal or near-optimal solution.

### 2 - PMX Crossover

The genetic algorithm incorporates the PMX crossover technique to create diverse offspring. PMX ensures that the child solutions inherit parts of their parents' paths, preserving the integrity of the route while introducing variability. This enhances the algorithm's ability to explore and converge towards optimal solutions.

### 3 - Mutation Probability

The genetic algorithm incorporates a mutation mechanism with adjustable probability. Mutation introduces diversity in the population by randomly altering some solutions, preventing premature convergence to suboptimal solutions. Users can fine-tune the mutation probability to strike a balance between exploration and exploitation.

### 4 - Population

The genetic algorithm maintains a population of potential solutions, evolving them over generations. A diverse population helps the algorithm explore a broader solution space. Users can configure the size of the population based on the characteristics of the TSP instance, allowing for flexibility in handling different problem complexities.

### 5 - Max Time of Execution

To control the execution time of the algorithm, a maximum time parameter is provided. This ensures that the algorithm terminates gracefully even if an optimal solution is not found within a specified timeframe. Users can set this parameter to meet specific time constraints, making the solution adaptable to different scenarios.

---

## Showcase 🔭

|                                   |                                     |
|:---------------------------------:|:-----------------------------------:|
| ![Start](./assets/showcase/1.png) | ![Choice1](./assets/showcase/3.png) |
| ![Start](./assets/showcase/2.png) | ![Choice1](./assets/showcase/4.png) |

---

## How it works? 🛠️

### Parameters

To execute the TSP solver you need to fill the following parameters:

|           Param           | Example  |                       Description                        |
|:-------------------------:|:--------:|:--------------------------------------------------------:| 
|      ```fileName```       | ex13.txt | Already gets from "files" folder, just put the file name |
|    ```threadsNumber```    |    4     |                                                          |
|     ```maxExecTime```     |    60    |                        in seconds                        |
|  ```populationNumber```   |   100    |                                                          |
| ```mutationProbability``` |   0.01   |                      from 0.01 to 1                      |

Final Result:
```Java
>  <fileName> <threadsNumber> <maxExecTime> <populationNumber> <mutationProbability> 
```
Example
```Java
>  ex13.txt 4 60 100 0.01 
```

### Results
|   File    | Best distance |
|:---------:|:-------------:|
|    ex5    |      21       |
|    ex6    |      23       |
|    ex7    |      105      |
|    ex8    |      244      |
|    ex9    |     1472      |
|   ex10    |      413      |
|   sp11    |      133      |
|   uk12    |     1733      |
|   ex13    |     3158      |
|  burma14  |     3323      |
|   lau15   |      291      |
| ulysses16 |     6859      |
|   gr17    |     2085      |
| ulysses22 |     7013      |
|   gr24    |     1272      |
|   fri26   |      937      |
| dantzig42 |      699      |
|   att48   |     33523     |


---

## Author 🤝

> This project was forked from Miguel Rolo's TSP project and adapted by me.

---

## License 🪪

> **(Apache License, Version 2.0)** You're **free to use** this content and codes in any project, personal or commercial. 
>
> There's no need to ask permission before using theses. Giving attribution is not required, but appreciated.