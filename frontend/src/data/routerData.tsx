import MainPage from '../pages/MainPage';
import LoginPage from '../pages/LoginPage';
import SignupPage from '../pages/SignupPage';
import FoundListPage from '../pages/FoundListPage';
import LostListPage from '../pages/LostListPage';
import ItemPage from '../pages/ItemPage';
import FindAccountPage from '../pages/FindAccountPage';
import FoundRegistPage from '../pages/FoundRegistPage';
import LostRegistPage from '../pages/LostRegistPage';

const routerData = [
  {
    id: 0,
    path: '/',
    lable: 'MainPage',
    element: <MainPage />,
    withAuth: false,
  },
  {
    // 로그인 한 경우 접근불가
    id: 1,
    path: '/login',
    lable: 'LoginPage',
    element: <LoginPage />,
    withAuth: false,
  },
  {
    // 로그인 한 경우 접근불가
    id: 2,
    path: '/signup',
    lable: 'SignupPage',
    element: <SignupPage />,
    withAuth: false,
  },
  {
    // 로그인 한 경우 접근불가
    id: 3,
    path: '/findid',
    lable: 'FindAccountPage',
    element: <FindAccountPage />,
    withAuth: false,
  },
  {
    id: 4,
    path: '/foundlist',
    lable: 'FoundListPage',
    element: <FoundListPage />,
    withAuth: false,
  },
  {
    id: 5,
    path: '/lostlist',
    lable: 'LostListPage',
    element: <LostListPage />,
    withAuth: false,
  },
  {
    id: 6,
    path: '/item',
    lable: 'ItemPage',
    element: <ItemPage />,
    withAuth: false,
  },
  {
    id: 7,
    path: '/found-regist',
    lable: 'FoundRegistPage',
    element: <FoundRegistPage />,
    withAuth: true,
  },
  {
    id: 8,
    path: '/lost-regist',
    lable: 'LostRegistPage',
    element: <LostRegistPage />,
    withAuth: true,
  },
];

export default routerData;
