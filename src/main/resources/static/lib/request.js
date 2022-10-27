import axios from '../lib/axios.min.js'


var service = axios.create();

	//请求前拦截
	service.interceptors.request.use(
		config => {
			config.headers.Token = localStorage.getItem("token")
			alert("localStorage"+localStorage.getItem("token"))
			return config;
		},
		error => {
			return Promise.reject(error);
		}
	);


    service.interceptors.response.use(
        response => {
            return response;
        },
        error => {
            if (error.response.status === 401) {
                location.href = '/login.html'
                return Promise.reject(error);
            }
            if (error.response.status === 500) {
                return Promise.reject(error.response);
            }
            return Promise.reject(error.response);
        }
    );

export default service