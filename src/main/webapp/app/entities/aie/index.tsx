import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AIE from './aie';
import AIEDetail from './aie-detail';
import AIEUpdate from './aie-update';
import AIEDeleteDialog from './aie-delete-dialog';

const AIERoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AIE />} />
    <Route path="new" element={<AIEUpdate />} />
    <Route path=":id">
      <Route index element={<AIEDetail />} />
      <Route path="edit" element={<AIEUpdate />} />
      <Route path="delete" element={<AIEDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AIERoutes;
