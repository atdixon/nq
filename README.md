#### Specialized N-Queens

To execute solver:

    gradle run --args 8
    gradle run --args '8 dfs'
    
The first command is more effective for larger values of N,
but does not guarantee a solution; the "dfs" approach is too
slow for non-small values of N, but guarantees a solution.

rand N=30 30s
dfs N=30 >1m

rand N=50 
