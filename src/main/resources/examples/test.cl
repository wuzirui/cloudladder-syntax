let q = [];
let vis = [
    [false, false, false],
    [false, false, false],
    [false, false, false]
];

function fibo(x) {
    if (x == 0 || x == 1) {
        return 1 + x + x * x;
    }

    return fibo(x - 1) + fibo(x - 2);
}

q = -1;
let result = fibo(q);
print(result);
//let sum = 0;
//for (let i = 1; i < result; i=i+1) {
//    print(i);
//    sum += i;
//}
