import axios from 'axios'


axios.defaults.withCredentials=true;

const service = axios.create({
    baseURL: import.meta.env.VITE_BASE_API,
    timeout: 30000
});

//请求前拦截
service.interceptors.request.use(
    config => {
        config.headers.Authorization = 'Bearer ' + localStorage.getItem("token")
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

//请求后拦截
service.interceptors.response.use(
    response => {
        const res = response.data;
        res.code = 200
        if (res.code !== 200) {
            alert(res.message)
            return Promise.reject(new Error(res.message || "Error"));
        } else {
            return res;
        }
    },
    error => {
        if(error.response.status === 401){
            alert("登入失败，请重新登入")
			 location.assign("/login");
            return Promise.reject(error);
        }
        if(error.response.status === 404){
            alert(error.response.data.message)
            return Promise.reject(error);
        }
        
    }
);

export default service