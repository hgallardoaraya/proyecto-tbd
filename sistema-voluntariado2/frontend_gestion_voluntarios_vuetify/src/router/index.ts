import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import LoggedInLayout from '@/layouts/LoggedInLayout.vue'
import NotLoggedInLayout from '@/layouts/NotLoggedInLayout.vue'
import { TaskListView, TaskView, CreateTaskView, EditTaskView, HomeView, LoginView, RegisterView, MapView, MapTasksView, LogOutView, MapEmergenciesView } from '@/views/index'
import { useAuth } from '@/store/auth'

interface RouteMeta extends Record<string, unknown> {
	requireAuth?: boolean;
	roles?: string[];
}

const routes: Array<RouteRecordRaw> = [
	{
		path: '/',
		redirect: {path: '/login'}
	},
	{
		path: '/home',
		name: 'home',
		component: HomeView,
		meta: {
			layout: LoggedInLayout,
			requireAuth: true,
			roles: ['COORDINADOR', 'VOLUNTARIO']
		}
	},
	{
		path: '/tasks',
		name: 'tasks',
		component: TaskListView,
		meta: {
			layout: LoggedInLayout,
			requireAuth: true,
			roles: ['COORDINADOR']
		}
	},
	{
		path: '/tasks/create',
		name: 'createTask',
		component: CreateTaskView,
		meta: {
			layout: LoggedInLayout,
			requireAuth: true,
			roles: ['COORDINADOR']
		}
	},
	{
		path: '/tasks/:id',
		name: 'task',
		component: TaskView,
		meta: {
			layout: LoggedInLayout,
			requireAuth: true,
			roles: ['COORDINADOR']
		}
	},
	{
		path: '/tasks/:id/edit',
		name: 'editTask',
		component: EditTaskView,
		meta: {
			layout: LoggedInLayout,
			requireAuth: true,
			roles: ['COORDINADOR']
		}
	},
	{
		path: '/emergencies/:id/map',
		name: 'map',
		component: MapView,
		meta: {
			layout: LoggedInLayout,
			requireAuth: true,
			roles: ['COORDINADOR']
		}
	},
	{
		path: '/tasks/map',
		name: 'tasksMap',
		component: MapTasksView,
		meta: {
			layout: LoggedInLayout,
			requireAuth: true,
			roles: ['COORDINADOR']
		}
	},
	{
		path: '/emergencies/map',
		name: 'emergenciesMap',
		component: MapEmergenciesView,
		meta: {
			layout: LoggedInLayout,
			requireAuth: false,
			roles: ['COORDINADOR']
		}
	},
	{
		path: '/login',
		name: 'login',
		component: LoginView,
		meta: {
			layout: NotLoggedInLayout,
			requireAuth: false,
			roles: []
		}
	},
	{
		path: '/register',
		name: 'register',
		component: RegisterView,
		meta: {
			layout: NotLoggedInLayout,
			requireAuth: false,
			roles: []
		}
	},
	{
		path: '/logout',
		name: 'logout',
		component: LogOutView,
		meta: {
			layout: NotLoggedInLayout,
			requireAuth: false,
			roles: []
		}
	}
]

const router = createRouter({
	history: createWebHistory(process.env.BASE_URL),
	routes
})

router.beforeEach((to) => {
	const auth = useAuth();
	const meta = to.meta as RouteMeta;		
	if(auth.tokenPayload.exp){
		if(auth.tokenPayload.exp * 1000 < new Date().getTime()){			
			auth.logout();
		}
	}
	if(meta.requireAuth && auth.token == null){		
		return { name: 'login' }
	} 
	else if(meta.requireAuth && meta.roles?.find(r => r === auth.tokenPayload.rol?.nombre)){		
		return true;
	} 
	else if(meta.requireAuth && !meta.roles?.find(r => r === auth.tokenPayload.rol?.nombre)){		
		alert('No posee permisos');
		return false;
	} 
	else if(to.name == 'login' && auth.token != null){
		return { name: 'tasks' }
	}
	return true;
})

export default router