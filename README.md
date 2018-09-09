#### Specialized N-Queens

To execute solver:

    gradle run --args 8
    gradle run --args '8 heuristic'
    
The latter will execute a randomized heuristic based solver that may perform
better than the default (dfs-based) solver for larger values of N.

NOTE: the solver restricts solutions to a specialized class that includes
no three colinear queen placements. 